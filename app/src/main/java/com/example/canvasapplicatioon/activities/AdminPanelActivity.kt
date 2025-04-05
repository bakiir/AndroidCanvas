package com.example.canvasapplicatioon.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.canvasapplicatioon.databinding.ActivityAdminPanelBinding

class AdminPanelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminPanelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUsers.setOnClickListener {
            startActivity(Intent(this@AdminPanelActivity, AdminUsersActivity::class.java))
        }

        binding.btnCourses.setOnClickListener {
            startActivity(Intent(this, AdminCoursesActivity::class.java))
        }
    }
}
