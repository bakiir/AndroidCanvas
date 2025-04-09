package com.example.canvasapplicatioon.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.canvasapplicatioon.databinding.ActivityLoginBinding
import com.example.canvasapplicatioon.api.ApiService
import com.example.canvasapplicatioon.models.LoginRequest
import com.example.canvasapplicatioon.models.LoginResponse
import com.example.canvasapplicatioon.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var apiService: ApiService
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiService.create()
        sessionManager = SessionManager(this)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)
            } else {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(email: String, password: String) {
        val request = LoginRequest(email, password)
        apiService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    sessionManager.saveUser(user)

                    when (user.role) {
                        "admin" -> startActivity(Intent(this@LoginActivity, AdminPanelActivity::class.java))
                        "teacher" -> startActivity(Intent(this@LoginActivity, TeacherCoursesActivity::class.java))
                        "student" -> startActivity(Intent(this@LoginActivity, StudentCoursesActivity::class.java))
                        else -> Toast.makeText(this@LoginActivity, "Unknown role", Toast.LENGTH_SHORT).show()
                    }

                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
