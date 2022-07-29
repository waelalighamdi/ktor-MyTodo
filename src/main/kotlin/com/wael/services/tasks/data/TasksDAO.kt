package com.wael.services.tasks.data

import com.wael.services.tasks.domain.Task

interface TasksDAO {
    suspend fun insertTask(task: Task): Boolean
    suspend fun getTasksByUserId(userId: String): List<Task>
    suspend fun getTaskByTaskId(taskId: String): Task?
    suspend fun updateTaskDescriptionByTaskId(taskId: String, description: String): Boolean
    suspend fun updateTaskStateByTaskId(taskId: String, state: Boolean): Boolean
    suspend fun updateTaskEndDateByTaskId(taskId: String, endDate: String): Boolean
    suspend fun deleteTask(taskId: String): Boolean
}