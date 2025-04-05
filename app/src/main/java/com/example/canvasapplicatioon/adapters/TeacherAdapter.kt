package com.example.canvasapplicatioon.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasapplicatioon.databinding.ItemUserBinding
import com.example.canvasapplicatioon.models.User

class TeacherAdapter(
    private val teacherList: List<User>,
    private val onTeacherSelected: (User) -> Unit,

) : RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeacherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        val teacher = teacherList[position]
        holder.bind(teacher)
    }

    override fun getItemCount(): Int {
        return teacherList.size
    }

    inner class TeacherViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teacher: User) {
            binding.nameTextView.text = teacher.name
            binding.emailTextView.text = teacher.email

            binding.root.setOnClickListener {
                onTeacherSelected(teacher)
            }
        }
    }
}
