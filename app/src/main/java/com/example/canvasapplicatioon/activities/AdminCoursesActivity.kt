package com.example.canvasapplicatioon.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.canvasapplicatioon.adapters.CourseAdapter
import com.example.canvasapplicatioon.api.ApiService
import com.example.canvasapplicatioon.databinding.ActivityAdminCoursesBinding
import com.example.canvasapplicatioon.databinding.DialogAddCourseBinding
import com.example.canvasapplicatioon.models.Course
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminCoursesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminCoursesBinding
    private lateinit var adapter: CourseAdapter
    private val apiService = ApiService.create()
    private val courseList = mutableListOf<Course>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка RecyclerView
        adapter = CourseAdapter(courseList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Обработка кликов
        adapter.onDeleteClick = { course ->
            confirmDelete(course)
        }

        binding.btnAddCourse.setOnClickListener {
            showAddCourseDialog()
        }

        // Загрузка курсов
        loadCourses()
    }

    private fun loadCourses() {
        apiService.getCourses().enqueue(object : Callback<List<Course>> {
            override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                if (response.isSuccessful && response.body() != null) {
                    courseList.clear()
                    courseList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@AdminCoursesActivity, "Ошибка загрузки курсов", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                Toast.makeText(this@AdminCoursesActivity, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showAddCourseDialog() {
        val dialogBinding = DialogAddCourseBinding.inflate(LayoutInflater.from(this))
        AlertDialog.Builder(this)
            .setTitle("Добавить курс")
            .setView(dialogBinding.root)
            .setPositiveButton("Добавить") { _, _ ->
                val name = dialogBinding.etCourseName.text.toString()
                val desc = dialogBinding.etCourseDesc.text.toString()
                if (name.isNotBlank()) {
                    val newCourse = Course(name = name, description = desc)
                    addCourse(newCourse)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun addCourse(course: Course) {
        apiService.addCourse(course).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AdminCoursesActivity, "Курс добавлен", Toast.LENGTH_SHORT).show()
                    loadCourses()
                } else {
                    Toast.makeText(this@AdminCoursesActivity, "Ошибка при добавлении", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AdminCoursesActivity, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun confirmDelete(course: Course) {
        AlertDialog.Builder(this)
            .setTitle("Удалить курс")
            .setMessage("Удалить курс ${course.name}?")
            .setPositiveButton("Удалить") { _, _ -> deleteCourse(course.id ?: -1) }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun deleteCourse(courseId: Long) {
        apiService.deleteCourse(courseId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AdminCoursesActivity, "Курс удалён", Toast.LENGTH_SHORT).show()
                    loadCourses()
                } else {
                    Toast.makeText(this@AdminCoursesActivity, "Ошибка при удалении", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AdminCoursesActivity, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
