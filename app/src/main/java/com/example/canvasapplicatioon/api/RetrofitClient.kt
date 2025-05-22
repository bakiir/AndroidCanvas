package com.example.canvasapplicatioon.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://a1c1-91-214-174-164.ngrok-free.app" // Укажи свой URL

    fun getInstance(): Retrofit {
        return retrofit
    }
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
