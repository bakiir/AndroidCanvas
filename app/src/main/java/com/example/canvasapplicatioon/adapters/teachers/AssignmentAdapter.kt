package com.example.canvasapplicatioon.adapters.teachers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasapplicatioon.models.Assignment
import com.example.canvasapplicatioon.databinding.ItemAssignmentBinding


class AssignmentAdapter(
    private var assignments: List<Assignment>,
    private val onDeleteClick: (Long) -> Unit,
    private val isStudent: Boolean // добавь этот параметр

) : RecyclerView.Adapter<AssignmentAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAssignmentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAssignmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val assignment = assignments[position]
        holder.binding.apply {
            tvTitle.text = assignment.title
            tvDescription.text = assignment.description
            tvDeadline.text = "До ${assignment.deadline}" // Форматируйте дату по желанию

            btnDelete.visibility = if (isStudent) View.GONE else View.VISIBLE

            btnDelete.setOnClickListener {
                assignment.id?.let { id -> onDeleteClick(id) }
            }
        }
    }

    override fun getItemCount() = assignments.size

    fun updateList(newList: List<Assignment>) {
        assignments = newList
        notifyDataSetChanged()
    }
}