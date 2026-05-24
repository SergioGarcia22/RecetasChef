const express = require('express');
const router  = express.Router();
const ctrl    = require('../controllers/recetasController');

router.get('/',               ctrl.listarRecetas);
router.get('/:id',            ctrl.obtenerReceta);
router.get('/:id/stats',      ctrl.obtenerStats);
router.post('/',              ctrl.crearReceta);
router.post('/:id/opiniones', ctrl.agregarOpinion);

module.exports = router;