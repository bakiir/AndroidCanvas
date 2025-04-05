package com.example.canvasapplicatioon.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.canvasapplicatioon.adapters.UsersAdapter
import com.example.canvasapplicatioon.api.ApiService
import com.example.canvasapplicatioon.api.RetrofitClient
import com.example.canvasapplicatioon.models.User
import com.example.canvasapplicatioon.databinding.ActivityAdminUsersBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdminUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminUsersBinding
    private lateinit var apiService: ApiService
    private val userList = mutableListOf<User>()
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Инициализация apiService до использования
        apiService = RetrofitClient.getInstance().create(ApiService::class.java)

        // Инициализация адаптера с обработчиками
        usersAdapter = UsersAdapter(userList, { user ->
            deleteUser(user)
        }, { teacher ->
            // Обработка назначения преподавателя
            val intent = Intent(this, AdminCoursesActivity::class.java)
            intent.putExtra("teacher_id", teacher.id)
            startActivity(intent)
        }, showAssignButton = false)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = usersAdapter

        // Загрузка пользователей
        getUsers()

        // ✅ Установить обработчик клика на кнопку для добавления пользователя
        binding.addUserButton.setOnClickListener {
            onAddUserClick() // Вызов функции для добавления пользователя
        }
    }



    private fun getUsers() {
        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    userList.clear()
                    response.body()?.let { userList.addAll(it) }
                    usersAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@AdminUsersActivity, "Failed to load users", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@AdminUsersActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onAddUserClick() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val role = binding.roleEditText.text.toString()

        val button = binding.addUserButton

        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && role.isNotEmpty()) {
            val user = User(name = name, email = email, password = password, role = role)
            addUser(user)
        } else {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
        }
        button.setOnClickListener {
            onAddUserClick()
        }
    }




    private fun addUser(user: User) {
        apiService.addUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val addedUser = response.body()
                    if (addedUser != null) {
                        userList.add(addedUser)
                        usersAdapter.notifyItemInserted(userList.size - 1)
                        Toast.makeText(this@AdminUsersActivity, "Пользователь добавлен", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@AdminUsersActivity, "Ошибка при добавлении", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@AdminUsersActivity, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteUser(user: User) {

        apiService.deleteUser(user.id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AdminUsersActivity, "User deleted", Toast.LENGTH_SHORT).show()
                    getUsers()  // Обновляем список пользователей после удаления
                } else {
                    Toast.makeText(this@AdminUsersActivity, "Failed to delete user", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AdminUsersActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
