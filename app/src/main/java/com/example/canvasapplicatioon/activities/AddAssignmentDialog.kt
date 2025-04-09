package com.example.canvasapplicatioon.activities

import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Toast
import com.example.canvasapplicatioon.api.ApiService
import com.example.canvasapplicatioon.api.RetrofitClient
import com.example.canvasapplicatioon.models.Assignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.canvasapplicatioon.databinding.DialogAddAssignmentBinding


class AddAssignmentDialog(
    private val context: Context,
    private val courseId: Long,
    private val onAssignmentAdded: () -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogAddAssignmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAddAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnAdd.setOnClickListener {
                val calendar = Calendar.getInstance().apply {
                    set(
                        deadlinePicker.year,
                        deadlinePicker.month,
                        deadlinePicker.dayOfMonth,
                        timePicker.hour,
                        timePicker.minute
                    )
                }

                val assignment = Assignment(
                    title = etTitle.text.toString(),
                    description = etDescription.text.toString(),
                    deadline = calendar.time,
//                    courseId = courseId
                )

                RetrofitClient.getInstance()
                    .create(ApiService::class.java)
                    .addAssignment(courseId, assignment)
                    .enqueue(object : Callback<Assignment> {
                        override fun onResponse(call: Call<Assignment>, response: Response<Assignment>) {
                            if (response.isSuccessful) {
                                onAssignmentAdded()
                                dismiss()
                            } else {
                                Toast.makeText(context, "Ошибка добавления", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Assignment>, t: Throwable) {
                            Toast.makeText(context, "Ошибка сети", Toast.LENGTH_SHORT).show()
                        }
                    })
            }

            btnCancel.setOnClickListener { dismiss() }
        }
    }


}