package com.example.canvasapplicatioon.adapters.students

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasapplicatioon.models.Course
import com.example.canvasapplicatioon.databinding.ItemStudentCourseBinding


class StudentCourseAdapter(
    private var courses: List<Course>,
    private val onOpenClick: (Course) -> Unit
) : RecyclerView.Adapter<StudentCourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(val binding: ItemStudentCourseBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemStudentCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.binding.tvCourseTitle.text = course.name

        holder.binding.btnOpenCourse.setOnClickListener {
            onOpenClick(course)
        }
    }

    fun updateList(newList: List<Course>) {
        courses = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = courses.size
}
