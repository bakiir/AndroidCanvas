package com.example.canvasapplicatioon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasapplicatioon.models.Course
import com.example.canvasapplicatioon.databinding.ItemCourseTeacherBinding


class TeacherCoursesAdapter(
    private val courses: List<Course>,
    private val onEnterClick: (Course) -> Unit
) : RecyclerView.Adapter<TeacherCoursesAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(val binding: ItemCourseTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: Course) {
            binding.courseNameText.text = course.name
            binding.enterCourseButton.setOnClickListener {
                onEnterClick(course)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseTeacherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    override fun getItemCount(): Int = courses.size
}

