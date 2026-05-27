package com.example.recetaschef.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Para emulador usa 10.0.2.2, para dispositivo físico usa la IP del PC
    private const val BASE_URL = "http://10.0.2.2:3000/"

    val apiService: RecipeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeApiService::class.java)
    }
}