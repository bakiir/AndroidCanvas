package com.example.canvasapplicatioon.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.canvasapplicatioon.adapters.teachers.StudentAdapter
import com.example.canvasapplicatioon.api.ApiService
import com.example.canvasapplicatioon.api.RetrofitClient
import com.example.canvasapplicatioon.databinding.ActivityCourseDetailsBinding
import com.example.canvasapplicatioon.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourseDetailsBinding
    private lateinit var apiService: ApiService
    private lateinit var studentAdapter: StudentAdapter // Переменная для адаптера студентов

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = RetrofitClient.getInstance().create(ApiService::class.java)

        // Инициализация адаптера с пустым списком
        studentAdapter = StudentAdapter(emptyList(), showCheckbox = false)
        binding.studentsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.studentsRecyclerView.adapter = studentAdapter

        val courseId = intent.getLongExtra("course_id", -1L)
        if (courseId == null) {
            Toast.makeText(this, "Ошибка: курс не найден", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.courseTitleText.text = "Курс ID: $courseId"

        // Кнопка "Добавить задание"
        binding.addAssignmentButton.setOnClickListener {
            Toast.makeText(this, "Добавить задание для курса $courseId", Toast.LENGTH_SHORT).show()
            // Или: startActivity(Intent(this, AddAssignmentActivity::class.java))
        }

        // Подгружаем студентов
        loadStudents(courseId)
    }

    private fun loadStudents(courseId: Long) {
        apiService.getStudentsByCourse(courseId).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val students = response.body() ?: emptyList()
                    // Обновляем адаптер с новыми данными
                    studentAdapter = StudentAdapter(students, showCheckbox = false)
                    binding.studentsRecyclerView.adapter = studentAdapter
                } else {
                    Toast.makeText(this@CourseDetailsActivity, "Ошибка загрузки студентов", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@CourseDetailsActivity, "Сервер недоступен", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
