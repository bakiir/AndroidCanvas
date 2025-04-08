package com.example.canvasapplicatioon.activities

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import com.example.canvasapplicatioon.databinding.DialogStudentSelectionBinding
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.canvasapplicatioon.adapters.StudentSelectionAdapter
import com.example.canvasapplicatioon.api.ApiService
import com.example.canvasapplicatioon.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentSelectionDialogFragment(
    private val courseId: Long,
    private val onStudentsSelected: (List<Long>) -> Unit
) : DialogFragment() {

    private var _binding: DialogStudentSelectionBinding? = null
    private val binding get() = _binding!!

    private val selectedIds = mutableSetOf<Long>()
    private lateinit var adapter: StudentSelectionAdapter
    private val api = ApiService.create()  // или RetrofitClient.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogStudentSelectionBinding.inflate(layoutInflater)

        // Настройка RecyclerView
        adapter = StudentSelectionAdapter { id, checked ->
            if (checked) selectedIds.add(id) else selectedIds.remove(id)
        }
        binding.studentRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.studentRecyclerView.adapter = adapter

        // Загрузка студентов
        loadStudents()

        // Кнопки
        binding.cancelButton.setOnClickListener { dismiss() }
        binding.confirmButton.setOnClickListener {
            onStudentsSelected(selectedIds.toList())
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }

    private fun loadStudents() {
        binding.progressBar.visibility = View.VISIBLE
        api.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, resp: Response<List<User>>) {
                binding.progressBar.visibility = View.GONE
                val students = resp.body()
                    ?.filter { it.role == "student" }
                    ?: emptyList()
                adapter.setStudents(students)
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
