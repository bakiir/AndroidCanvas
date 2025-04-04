package com.example.canvasapplicatioon.api

import com.example.canvasapplicatioon.models.Course
import com.example.canvasapplicatioon.models.LoginRequest
import com.example.canvasapplicatioon.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/auth/login")
    fun login(@Body request: LoginRequest): retrofit2.Call<LoginResponse>
    @GET("/admin/courses")
    fun getCourses(): Call<List<Course>>

    @POST("/admin/courses")
    fun createCourse(@Body course: Course): Call<Void>

    @POST("/courses/{courseId}/students/{studentId}")
    fun addStudentToCourse(
        @Path("courseId") courseId: Int,
        @Path("studentId") studentId: Int
    ): Call<Void>



}


