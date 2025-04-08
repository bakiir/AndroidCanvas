package com.example.canvasapplicatioon.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://beb9-95-82-118-94.ngrok-free.app" // Укажи свой URL

    fun getInstance(): Retrofit {
        return retrofit
    }
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
