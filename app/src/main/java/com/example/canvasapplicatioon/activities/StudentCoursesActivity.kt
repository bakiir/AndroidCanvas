package com.example.canvasapplicatioon.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.canvasapplicatioon.adapters.students.StudentCourseAdapter
import com.example.canvasapplicatioon.api.ApiService
import com.example.canvasapplicatioon.api.RetrofitClient
import com.example.canvasapplicatioon.databinding.ActivityStudentCoursesBinding
import com.example.canvasapplicatioon.models.Course
import com.example.canvasapplicatioon.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StudentCoursesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentCoursesBinding
    private lateinit var apiService: ApiService
    private lateinit var adapter: StudentCourseAdapter
    private lateinit var sessionManager: SessionManager
    private var studentId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        studentId = sessionManager.getUserId() // Убедитесь, что здесь корректное значение

        apiService = RetrofitClient.getInstance().create(ApiService::class.java)

        // Инициализация адаптера
        adapter = StudentCourseAdapter(emptyList()) { course ->
            val intent = Intent(this, StudentCourseDetailActivity::class.java)
            intent.putExtra("COURSE_ID", course.id)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        loadCourses() // Загружаем курсы после инициализации адаптера
    }

    private fun loadCourses() {
        apiService.getCoursesForStudent(studentId).enqueue(object : Callback<List<Course>> {
            override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                if (response.isSuccessful) {
                    adapter.updateList(response.body() ?: emptyList())
                } else {
                    Toast.makeText(this@StudentCoursesActivity, "Ошибка загрузки курсов", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                Toast.makeText(this@StudentCoursesActivity, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
