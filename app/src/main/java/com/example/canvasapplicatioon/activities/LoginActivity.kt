package com.example.canvasapplicatioon.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.canvasapplicatioon.R
import com.example.canvasapplicatioon.api.ApiService
import com.example.canvasapplicatioon.models.LoginRequest
import com.example.canvasapplicatioon.models.LoginResponse
import com.example.canvasapplicatioon.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var session: SessionManager
    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        session = SessionManager(this)
        api = Retrofit.Builder()
            .baseUrl("https://your-backend-url.com") // Замените на URL вашего бэкенда
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val pass = passEditText.text.toString()

            val loginRequest = LoginRequest(email, pass)

            api.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val loginRes = response.body()!!
                        session.saveAuthData(loginRes.token, loginRes.role, loginRes.userId)

                        when (loginRes.role.lowercase()) {
                            "admin" -> startActivity(Intent(this@LoginActivity, AdminPanelActivity::class.java))
//                            "teacher" -> startActivity(Intent(this@LoginActivity, TeacherPanelActivity::class.java))
//                            "student" -> startActivity(Intent(this@LoginActivity, StudentPanelActivity::class.java))
                            else -> Toast.makeText(this@LoginActivity, "Неизвестная роль", Toast.LENGTH_SHORT).show()
                        }
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Ошибка входа", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}