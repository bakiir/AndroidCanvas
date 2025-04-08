package com.example.canvasapplicatioon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.canvasapplicatioon.databinding.ItemStudentCheckboxBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasapplicatioon.models.User
class StudentSelectionAdapter(
    private val onCheckedChange: (Long, Boolean) -> Unit
) : RecyclerView.Adapter<StudentSelectionAdapter.VH>() {

    private val students = mutableListOf<User>()  // твой data class User(id, name, email, password, role)

    fun setStudents(list: List<User>) {
        students.clear()
        students.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(val binding: ItemStudentCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvStudentName.text = user.name
            binding.checkboxStudent.setOnCheckedChangeListener(null)
            binding.checkboxStudent.isChecked = false
            binding.checkboxStudent.setOnCheckedChangeListener { _, checked ->
                user.id?.let { onCheckedChange(it, checked) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemStudentCheckboxBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount() = students.size
}
