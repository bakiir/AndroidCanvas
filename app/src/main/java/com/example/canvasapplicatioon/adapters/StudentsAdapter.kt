package com.example.canvasapplicatioon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasapplicatioon.databinding.ItemStudentBinding
import com.example.canvasapplicatioon.models.User

class StudentsAdapter(
    private var users: List<User>
) : RecyclerView.Adapter<StudentsAdapter.VH>() {

    private val selectedIds = mutableSetOf<Long>()

    inner class VH(private val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.userName.text = user.name
            binding.userCheckBox.isChecked = selectedIds.contains(user.id)
            binding.userCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) selectedIds.add(user.id!!)
                else selectedIds.remove(user.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun getSelectedIds(): List<Long> = selectedIds.toList()

    fun updateData(newUsers: List<User>) {
        users = newUsers
        selectedIds.clear()
        notifyDataSetChanged()
    }
}
