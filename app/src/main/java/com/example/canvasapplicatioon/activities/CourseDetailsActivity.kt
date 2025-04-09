package com.example.canvasapplicatioon.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.canvasapplicatioon.adapters.teachers.StudentAdapter
import com.example.canvasapplicatioon.api.ApiService
import com.example.canvasapplicatioon.api.RetrofitClient
import com.example.canvasapplicatioon.databinding.ActivityCourseDetailsBinding
import com.example.canvasapplicatioon.models.Assignment
import com.example.canvasapplicatioon.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.canvasapplicatioon.adapters.teachers.AssignmentAdapter


class CourseDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourseDetailsBinding
    private lateinit var apiService: ApiService
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var assignmentsAdapter: AssignmentAdapter
    private var courseId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = RetrofitClient.getInstance().create(ApiService::class.java)
        courseId = intent.getLongExtra("course_id", -1L).takeIf { it != -1L }
            ?: run {
                Toast.makeText(this, "Ошибка: курс не найден", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

        setupRecyclerViews()
        loadData()

        binding.courseTitleText.text = "Курс ID: $courseId"

        binding.addAssignmentButton.setOnClickListener {
            showAddAssignmentDialog()
        }
    }

    private fun setupRecyclerViews() {
        // Настройка адаптера заданий
        assignmentsAdapter = AssignmentAdapter(emptyList()) { assignmentId ->
            deleteAssignment(assignmentId)
        }
        binding.assignmentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CourseDetailsActivity)
            adapter = assignmentsAdapter
        }

        // Настройка адаптера студентов
        studentAdapter = StudentAdapter(emptyList(), showCheckbox = false)
        binding.studentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CourseDetailsActivity)
            adapter = studentAdapter
        }
    }

    private fun loadData() {
        loadAssignments()
        loadStudents()
    }

    private fun showAddAssignmentDialog() {
        AddAssignmentDialog(this, courseId) {
            loadAssignments()
        }.show()
    }

    private fun loadAssignments() {
        apiService.getCourseAssignments(courseId).enqueue(object : Callback<List<Assignment>> {
            override fun onResponse(call: Call<List<Assignment>>, response: Response<List<Assignment>>) {
                if (response.isSuccessful) {
                    assignmentsAdapter.updateList(response.body() ?: emptyList())
                } else {
                    Toast.makeText(this@CourseDetailsActivity, "Ошибка загрузки заданий", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Assignment>>, t: Throwable) {
                Toast.makeText(this@CourseDetailsActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadStudents() {
        apiService.getStudentsByCourse(courseId).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    studentAdapter.updateList(response.body() ?: emptyList())
                } else {
                    Toast.makeText(this@CourseDetailsActivity, "Ошибка загрузки студентов", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@CourseDetailsActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteAssignment(assignmentId: Long) {
        apiService.deleteAssignment(assignmentId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    loadAssignments()
                    Toast.makeText(this@CourseDetailsActivity, "Задание удалено", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CourseDetailsActivity, "Ошибка удаления", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CourseDetailsActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}