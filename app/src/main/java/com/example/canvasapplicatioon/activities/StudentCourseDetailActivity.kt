package com.example.canvasapplicatioon.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.canvasapplicatioon.adapters.teachers.AssignmentAdapter
import com.example.canvasapplicatioon.api.ApiService
import com.example.canvasapplicatioon.api.RetrofitClient
import com.example.canvasapplicatioon.databinding.ActivityStudentCourseDetailBinding
import com.example.canvasapplicatioon.models.Assignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StudentCourseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentCourseDetailBinding
    private lateinit var apiService: ApiService
    private lateinit var adapter: AssignmentAdapter
    private var courseId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        courseId = intent.getLongExtra("course_id", -1L)
        if (courseId == -1L) {
            Toast.makeText(this, "Курс не найден", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        apiService = RetrofitClient.getInstance().create(ApiService::class.java)
        adapter = AssignmentAdapter(emptyList()) { /* Нет удаления у студента */ }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        loadAssignments()
    }

    private fun loadAssignments() {
        apiService.getCourseAssignments(courseId).enqueue(object : Callback<List<Assignment>> {
            override fun onResponse(call: Call<List<Assignment>>, response: Response<List<Assignment>>) {
                if (response.isSuccessful) {
                    adapter.updateList(response.body() ?: emptyList())
                } else {
                    Toast.makeText(this@StudentCourseDetailActivity, "Ошибка загрузки заданий", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Assignment>>, t: Throwable) {
                Toast.makeText(this@StudentCourseDetailActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
