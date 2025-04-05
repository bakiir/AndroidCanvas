package com.example.canvasapplicatioon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasapplicatioon.databinding.ItemCourseBinding
import com.example.canvasapplicatioon.models.Course

// В твоем адаптере для курсов (CourseAdapter)
class CourseAdapter(
    private val courseList: List<Course>,
    private val onDeleteClick: (Course) -> Unit,
    private val onAssignTeacherClick: (Course) -> Unit // Добавляем обработчик для назначения преподавателя
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

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
            binding.courseName.text = course.name
            binding.courseDescription.text = course.description

            // Обработчик для кнопки "Назначить преподавателя"
            binding.assignTeacherButton.setOnClickListener {
                onAssignTeacherClick(course)
            }

            // Обработчик для кнопки "Удалить"
            binding.deleteButton.setOnClickListener {
                onDeleteClick(course)
            }
        }
    }
}
