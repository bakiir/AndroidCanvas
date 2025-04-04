package com.example.canvasapplicatioon.utils

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun saveAuthData(token: String, role: String, userId: String) {
        prefs.edit().apply {
            putString("TOKEN", token)
            putString("ROLE", role)
            putString("USER_ID", userId)
            apply()
        }
    }

    fun getRole(): String? = prefs.getString("ROLE", null)
    fun getUserId(): String? = prefs.getString("USER_ID", null)
}
