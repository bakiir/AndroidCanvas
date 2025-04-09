package com.example.canvasapplicatioon.utils

import android.content.Context
import com.example.canvasapplicatioon.models.LoginResponse

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUser(user: LoginResponse) {
        prefs.edit()
            .putLong("id", user.id)
            .putString("name", user.name)
            .putString("email", user.email)
            .putString("role", user.role)
            .apply()
    }

    fun getUserId(): Long = prefs.getLong("id", -1L)


    fun getRole(): String? = prefs.getString("role", null)
}
