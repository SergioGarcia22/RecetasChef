const db = require('../config/db');

const listarRecetas = async (req, res) => {
  try {
    const [recetas] = await db.query(`
      SELECT r.*,
        ROUND(AVG(o.puntuacion), 1) AS puntuacion_promedio,
        COUNT(DISTINCT o.id)        AS total_opiniones,
        COUNT(DISTINCT p.id)        AS veces_preparada
      FROM recetas r
      LEFT JOIN opiniones o ON o.receta_id = r.id
      LEFT JOIN preparaciones p ON p.receta_id = r.id
      GROUP BY r.id
      ORDER BY r.creado_en DESC
    `);
    res.json(recetas);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

const obtenerReceta = async (req, res) => {
  try {
    const { id } = req.params;
    const [[receta]] = await db.query('SELECT * FROM recetas WHERE id = ?', [id]);
    if (!receta) return res.status(404).json({ error: 'Receta no encontrada' });

    const [ingredientes] = await db.query(
      'SELECT * FROM ingredientes WHERE receta_id = ? ORDER BY id', [id]
    );
    const [pasos] = await db.query(
      'SELECT * FROM pasos WHERE receta_id = ? ORDER BY numero_paso', [id]
    );
    const [opiniones] = await db.query(
      'SELECT * FROM opiniones WHERE receta_id = ? ORDER BY fecha DESC', [id]
    );
    res.json({ ...receta, ingredientes, pasos, opiniones });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

const obtenerStats = async (req, res) => {
  try {
    const { id } = req.params;
    const [[stats]] = await db.query(`
      SELECT
        r.nombre,
        r.foto_url,
        r.tiempo_preparacion,
        ROUND(AVG(o.puntuacion), 1) AS puntuacion_promedio,
        COUNT(DISTINCT o.id)        AS total_opiniones,
        COUNT(DISTINCT p.id)        AS veces_preparada,
        ROUND(AVG(p.porciones), 0)  AS porciones_promedio
      FROM recetas r
      LEFT JOIN opiniones o ON o.receta_id = r.id
      LEFT JOIN preparaciones p ON p.receta_id = r.id
      WHERE r.id = ?
      GROUP BY r.id
    `, [id]);

    const [historial] = await db.query(`
      SELECT p.fecha, p.porciones
      FROM preparaciones p
      WHERE p.receta_id = ?
      ORDER BY p.fecha DESC
      LIMIT 10
    `, [id]);

    res.json({ ...stats, historial });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

const crearReceta = async (req, res) => {
  const conn = await db.getConnection();
  try {
    await conn.beginTransaction();
    const { nombre, descripcion, categoria, tiempo_preparacion,
            porciones, dificultad, foto_url, ingredientes, pasos } = req.body;

    const [result] = await conn.query(
      `INSERT INTO recetas (nombre, descripcion, categoria, tiempo_preparacion,
        porciones, dificultad, foto_url) VALUES (?,?,?,?,?,?,?)`,
      [nombre, descripcion, categoria, tiempo_preparacion, porciones, dificultad, foto_url]
    );
    const recetaId = result.insertId;

    if (ingredientes?.length) {
      const vals = ingredientes.map(i => [recetaId, i.nombre, i.cantidad, i.unidad]);
      await conn.query(
        'INSERT INTO ingredientes (receta_id, nombre, cantidad, unidad) VALUES ?', [vals]
      );
    }
    if (pasos?.length) {
      const vals = pasos.map((p, idx) => [recetaId, idx + 1, p.descripcion]);
      await conn.query(
        'INSERT INTO pasos (receta_id, numero_paso, descripcion) VALUES ?', [vals]
      );
    }

    await conn.commit();
    res.status(201).json({ id: recetaId, mensaje: 'Receta creada' });
  } catch (err) {
    await conn.rollback();
    res.status(500).json({ error: err.message });
  } finally {
    conn.release();
  }
};

const agregarOpinion = async (req, res) => {
  try {
    const { id } = req.params;
    const { comensal, comentario, puntuacion, porciones_usadas } = req.body;

    if (!puntuacion || puntuacion < 1 || puntuacion > 5)
      return res.status(400).json({ error: 'Puntuación debe estar entre 1 y 5' });

    await db.query(
      `INSERT INTO opiniones (receta_id, comensal, comentario, puntuacion, porciones_usadas)
       VALUES (?,?,?,?,?)`,
      [id, comensal || 'Anónimo', comentario, puntuacion, porciones_usadas || 2]
    );
    await db.query(
      'INSERT INTO preparaciones (receta_id, porciones) VALUES (?,?)',
      [id, porciones_usadas || 2]
    );

    res.status(201).json({ mensaje: 'Opinión registrada' });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

module.exports = { listarRecetas, obtenerReceta, obtenerStats, crearReceta, agregarOpinion };