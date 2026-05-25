package com.example.recetaschef.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // ⚠️ 10.0.2.2 es el localhost del PC desde el emulador de Android Studio
    // Cambia el puerto 3000 si el backend usa otro
    private const val BASE_URL = "http://10.0.2.2:3000/"

    val apiService: RecipeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeApiService::class.java)
    }
}
