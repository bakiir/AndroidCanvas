package com.example.canvasapplicatioon.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasapplicatioon.databinding.ItemUserBinding
import com.example.canvasapplicatioon.models.User

class UsersAdapter(
    private val userList: MutableList<User>,
    private val onDeleteClick: (User) -> Unit,
    private val onAssignTeacherClick: (User) -> Unit = {},  // ← добавили дефолт
    private val showAssignButton: Boolean = false  // ← добавили флаг


) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.nameTextView.text = user.name
            binding.emailTextView.text = user.email

            // Показывать кнопку "Назначить", только если это разрешено
            if (showAssignButton && user.role == "teacher") {
                binding.assignTeacherButton.visibility = View.VISIBLE
                binding.assignTeacherButton.setOnClickListener {
                    onAssignTeacherClick(user)
                }
            } else {
                binding.assignTeacherButton.visibility = View.GONE
            }

            binding.deleteButton.setOnClickListener {
                onDeleteClick(user)
            }
        }

    }
}
