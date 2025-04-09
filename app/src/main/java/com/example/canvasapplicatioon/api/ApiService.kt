package com.example.canvasapplicatioon.api


import com.example.canvasapplicatioon.models.Assignment
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
    fun deleteCourse(@Path("courseId") courseId: Long?): Call<Void>

    //======================================================

    @POST("admin/courses/{courseId}/teachers/{teacherId}")
    fun assignTeacherToCourse(
        @Path("courseId") courseId: Long?,
        @Path("teacherId") teacherId: Long?
    ): Call<Void>

    @POST("/admin/courses/{courseId}/students")
    fun addStudentsToCourse(
        @Path("courseId") courseId: Long,
        @Body studentIds: List<Long>
    ): Call<Course>

    //======================================================

    @GET("/admin/courses/{courseId}/students")
    fun getStudentsByCourse(@Path("courseId") courseId: Long): Call<List<User>>

    @GET("/teacher/courses/{teacherId}")
    fun getCoursesForTeacher(@Path("teacherId") teacherId: Long): Call<List<Course>>

    @POST("/teacher/courses/{courseId}/assignments")
    fun addAssignment(
        @Path("courseId") courseId: Long,
        @Body assignment: Assignment
    ): Call<Assignment>

    @GET("/api/assignments/course/{courseId}")
    fun getCourseAssignments(@Path("courseId") courseId: Long): Call<List<Assignment>>

    @DELETE("/teacher/assignments/{assignmentId}")
    fun deleteAssignment(@Path("assignmentId") assignmentId: Long): Call<Void>

    companion object {
        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl("https://a4bc-95-82-117-169.ngrok-free.app") // локальный адрес для Android-эмулятора
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }

}
