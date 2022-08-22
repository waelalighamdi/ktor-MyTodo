package com.wael.services.tasks.domain

interface TasksRepository {
    suspend fun getTasksByUserId(userId: String): List<Task>
    suspend fun getTasksByUserIdAndPage(userId: String, limit: Int, offset: Int): List<Task>
    suspend fun insertTask(userId: String, title: String, description: String, category: String): Task?
    suspend fun isTaskIdExist(taskId: String): Boolean
    suspend fun updateTaskDescriptionByTaskId(taskId: String, description: String): Task?
    suspend fun updateTaskStateByTaskId(taskId: String, state: Boolean): Task?
    suspend fun updateTaskCompletionDateByTaskId(taskId: String, endDate: String): Task?
    suspend fun deleteTask(taskId: String): Boolean
}