package com.example.canvasapplicatioon.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.canvasapplicatioon.adapters.TeacherCoursesAdapter
import com.example.canvasapplicatioon.api.ApiService
import com.example.canvasapplicatioon.api.RetrofitClient
import com.example.canvasapplicatioon.models.Course
import com.example.canvasapplicatioon.databinding.ActivityTeacherCoursesBinding
import com.example.canvasapplicatioon.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TeacherCoursesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherCoursesBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        apiService = RetrofitClient.getInstance().create(ApiService::class.java)

        val userId = sessionManager.getUserId()
        if (userId != -1L) {
            getCoursesFromApi(userId)
        } else {
            Toast.makeText(this, "Ошибка: ID пользователя не найден", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCoursesFromApi(teacherId: Long) {
        apiService.getCoursesForTeacher(teacherId).enqueue(object : Callback<List<Course>> {
            override fun onResponse(
                call: Call<List<Course>>,
                response: Response<List<Course>>
            ) {
                if (response.isSuccessful) {
                    val courses = response.body() ?: emptyList()
                    val adapter = TeacherCoursesAdapter(courses) { selectedCourse ->
                        val intent = Intent(this@TeacherCoursesActivity, CourseDetailsActivity::class.java)
                        intent.putExtra("course_id", selectedCourse.id)
                        startActivity(intent)
                    }
                    binding.recyclerViewCourses.layoutManager = LinearLayoutManager(this@TeacherCoursesActivity)
                    binding.recyclerViewCourses.adapter = adapter
                } else {
                    Toast.makeText(this@TeacherCoursesActivity, "Ошибка загрузки курсов", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                Toast.makeText(this@TeacherCoursesActivity, "Сервер недоступен", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


