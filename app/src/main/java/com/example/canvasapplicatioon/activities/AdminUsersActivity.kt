package com.example.canvasapplicatioon.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.canvasapplicatioon.adapters.UsersAdapter
import com.example.canvasapplicatioon.api.ApiService
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

        apiService = ApiService.create()

        usersAdapter = UsersAdapter(userList) { user ->
            deleteUser(user)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = usersAdapter

        binding.addUserButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val role = binding.roleEditText.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && role.isNotEmpty()) {
                addUser(name, email, password, role)
            } else {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            }
        }

        getUsers()
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

    private fun addUser(name: String, email: String, password: String, role: String) {
        val newUser = User(name = name, email = email, password = password, role = role)  // Указание id = 0, так как бекенд, вероятно, генерирует id.
        apiService.addUser(newUser).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AdminUsersActivity, "User added", Toast.LENGTH_SHORT).show()
                    getUsers()  // Обновляем список пользователей после добавления
                } else {
                    Toast.makeText(this@AdminUsersActivity, "Failed to add user", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@AdminUsersActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
