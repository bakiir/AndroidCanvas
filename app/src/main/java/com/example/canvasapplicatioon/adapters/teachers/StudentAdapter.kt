package com.example.canvasapplicatioon.adapters.teachers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasapplicatioon.databinding.ItemStudentBinding
import com.example.canvasapplicatioon.models.User

class StudentAdapter(
    private val students: List<User>,
    private val showCheckbox: Boolean = false // по умолчанию чекбокс скрыт
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: User) {
            binding.userName.text = student.name
            binding.userCheckBox.visibility = if (showCheckbox) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount(): Int = students.size
}
