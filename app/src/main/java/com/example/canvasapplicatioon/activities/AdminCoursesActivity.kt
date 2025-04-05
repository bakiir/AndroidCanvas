package com.example.canvasapplicatioon.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.canvasapplicatioon.adapters.CourseAdapter
import com.example.canvasapplicatioon.adapters.TeacherAdapter
import com.example.canvasapplicatioon.api.ApiService
import com.example.canvasapplicatioon.databinding.ActivityAdminCoursesBinding
import com.example.canvasapplicatioon.databinding.DialogAddCourseBinding
import com.example.canvasapplicatioon.models.Course
import com.example.canvasapplicatioon.models.User
import com.example.canvasapplicatioon.databinding.DialogTeacherBinding
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
        // В AdminCoursesActivity
        adapter = CourseAdapter(courseList, { course ->
            confirmDelete(course)
        }, { course ->
            showAssignTeacherDialog(course.id)  // Вызываем метод для показа диалога
        }, )
        binding.recyclerView.adapter = adapter


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter



        binding.btnAddCourse.setOnClickListener {
            showAddCourseDialog()
        }

        // Загрузка курсов
        loadCourses()
    }


    // В AdminCoursesActivity
    private fun showAssignTeacherDialog(courseId: Long) {
        // Получаем всех пользователей с сервера
        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()

                    // Фильтруем пользователей, оставляем только учителей
                    val teachers = users.filter { it.role == "teacher" }

                    // Создаем диалог с RecyclerView для списка учителей
                    val dialogBinding = DialogTeacherBinding.inflate(layoutInflater)
                    val dialog = AlertDialog.Builder(this@AdminCoursesActivity)
                        .setView(dialogBinding.root)
                        .setTitle("Выберите учителя")
                        .setPositiveButton("Назначить") { _, _ ->
                            // Логика назначения учителя на курс
                        }
                        .setNegativeButton("Отмена", null)
                        .create()

                    // Настроим RecyclerView для списка учителей
                    val teacherAdapter = TeacherAdapter(teachers) { selectedTeacher ->
                        // Когда учитель выбран, передаем его для назначения
                        assignTeacherToCourse(courseId, selectedTeacher.id)
                        dialog.dismiss() // Закрываем диалог
                    }

                    dialogBinding.recyclerView.layoutManager = LinearLayoutManager(this@AdminCoursesActivity)
                    dialogBinding.recyclerView.adapter = teacherAdapter
                    dialog.show()
                } else {
                    Toast.makeText(this@AdminCoursesActivity, "Не удалось получить пользователей", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@AdminCoursesActivity, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun assignTeacherToCourse(courseId: Long, teacherId: Long) {
        apiService.assignTeacherToCourse(courseId, teacherId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AdminCoursesActivity, "Учитель назначен на курс", Toast.LENGTH_SHORT).show()
                    loadCourses() // Перезагружаем список курсов, чтобы отобразить изменения
                } else {
                    Toast.makeText(this@AdminCoursesActivity, "Ошибка при назначении учителя", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AdminCoursesActivity, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadCourses() {
        binding.progressBar.visibility = View.VISIBLE // Показываем индикатор загрузки

        apiService.getCourses().enqueue(object : Callback<List<Course>> {
            override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                binding.progressBar.visibility = View.GONE // Скрываем индикатор загрузки

                if (response.isSuccessful && response.body() != null) {
                    courseList.clear()
                    courseList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@AdminCoursesActivity, "Ошибка загрузки курсов", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE // Скрываем индикатор загрузки
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

    private fun showAssignTeacherDialog(courseId: Long?) {
        // Получаем всех пользователей с сервера
        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()

                    // Фильтруем пользователей, оставляем только учителей
                    val teachers = users.filter { it.role == "teacher" }

                    // Создаем диалог с RecyclerView для списка учителей
                    val dialogBinding = DialogTeacherBinding.inflate(layoutInflater)
                    val dialog = AlertDialog.Builder(this@AdminCoursesActivity)
                        .setView(dialogBinding.root)
                        .setTitle("Выберите учителя")
                        .setPositiveButton("Назначить") { _, _ ->
                            // Логика назначения учителя
                        }
                        .setNegativeButton("Отмена", null)
                        .create()

                    // Настроим RecyclerView для списка учителей
                    val teacherAdapter = TeacherAdapter(teachers) { selectedTeacher ->
                        // Когда учитель выбран, передаем его для назначения
                        assignTeacherToCourse(courseId, selectedTeacher.id)
                        dialog.dismiss() // Закрываем диалог
                    }

                    dialogBinding.recyclerView.layoutManager = LinearLayoutManager(this@AdminCoursesActivity)
                    dialogBinding.recyclerView.adapter = teacherAdapter
                    dialog.show()
                } else {
                    Toast.makeText(this@AdminCoursesActivity, "Не удалось получить пользователей", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@AdminCoursesActivity, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun assignTeacherToCourse(courseId: Long?, teacherId: Long?) {
        if (courseId != null && teacherId != null) {
            apiService.assignTeacherToCourse(courseId, teacherId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdminCoursesActivity, "Учитель назначен на курс", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AdminCoursesActivity, "Ошибка при назначении учителя", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@AdminCoursesActivity, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }




}
