package com.example.canvasapplicatioon.models

data class User(
    val id: Long? = null,
    val name: String,
    val email: String,
    val password: String,
    val role: String
)
