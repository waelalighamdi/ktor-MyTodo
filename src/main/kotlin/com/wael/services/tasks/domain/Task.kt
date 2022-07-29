package com.wael.services.tasks.domain

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val taskId: String,
    val userId: String,
    val title: String,
    val description: String,
    val category: String,
    val state: Boolean,
    val startDate: String,
    val endDate: String
)
