package com.example.canvasapplicatioon.api


import com.example.canvasapplicatioon.models.Course
import com.example.canvasapplicatioon.models.LoginRequest
import com.example.canvasapplicatioon.models.LoginResponse
import com.example.canvasapplicatioon.models.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>


    //======================================================

    @GET("/admin/users")
    fun getUsers(): Call<List<User>>

    @POST("/admin/users")
    fun addUser(@Body user: User): Call<User>

    @DELETE("/admin/users/{userId}")
    fun deleteUser(@Path("userId") userId: Long?): Call<Void>

    //======================================================

    // Получить список всех курсов
    @GET("/admin/courses")
    fun getCourses(): Call<List<Course>>

    // Добавить новый курс
    @POST("/admin/courses")
    fun addCourse(@Body course: Course): Call<Void>

    // Удалить курс по ID
    @DELETE("/admin/courses/{courseId}")
    fun deleteCourse(@Path("courseId") courseId: Long): Call<Void>

    //======================================================

    companion object {
        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl("https://43ee-95-82-117-141.ngrok-free.app") // локальный адрес для Android-эмулятора
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
