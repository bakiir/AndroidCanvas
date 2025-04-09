package com.example.canvasapplicatioon.models

import java.util.Date

data class Assignment(
    val id: Long? = null,
    val title: String,
    val description: String,
    val deadline: Date?,
//    val courseId: Long,
    )