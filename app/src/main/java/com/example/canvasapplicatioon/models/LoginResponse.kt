package com.example.canvasapplicatioon.models

data class LoginResponse(
    val userId: String,
    val role: String,
    val token: String
)

