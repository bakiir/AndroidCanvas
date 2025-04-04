package com.example.canvasapplicatioon.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasapplicatioon.R
import com.example.canvasapplicatioon.api.ApiService
import com.example.canvasapplicatioon.models.Course
import com.example.canvasapplicatioon.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var api: ApiService
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_panel)
        api = Retrofit.Builder()
            .baseUrl("https://your-backend-url.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        session = SessionManager(this)

        val buttonViewCourses = findViewById<Button>(R.id.buttonViewCourses)
        val buttonCreateCourse = findViewById<Button>(R.id.buttonCreateCourse)


        val buttonAddStudent = findViewById<Button>(R.id.buttonAddStudentToCourse)
        val editCourseId = findViewById<EditText>(R.id.editCourseId)
        val editStudentId = findViewById<EditText>(R.id.editStudentId)

        buttonAddStudent.setOnClickListener {
            val courseId = editCourseId.text.toString().toIntOrNull()
            val studentId = editStudentId.text.toString().toIntOrNull()

            if (courseId == null || studentId == null) {
                Toast.makeText(this, "Введите корректные ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            api.addStudentToCourse(courseId, studentId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdminPanelActivity, "Студент добавлен в курс", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AdminPanelActivity, "Ошибка: студент не добавлен", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@AdminPanelActivity, "Сетевая ошибка", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

}