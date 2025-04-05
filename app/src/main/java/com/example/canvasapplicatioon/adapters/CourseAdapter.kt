package com.example.canvasapplicatioon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasapplicatioon.databinding.ItemCourseBinding
import com.example.canvasapplicatioon.models.Course

class CourseAdapter(private val courseList: List<Course>, function: (Nothing) -> Unit) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    var onItemClick: ((Course) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        holder.bind(course)
    }

    override fun getItemCount(): Int = courseList.size

    inner class CourseViewHolder(private val binding: ItemCourseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(course: Course) {
            binding.tvCourseName.text = course.name
            binding.tvCourseDesc.text = course.description
            binding.root.setOnClickListener {
                onItemClick?.invoke(course)
            }
        }
    }
}
