package com.wael.services.tasks.domain

import com.wael.services.tasks.data.tasksDAO
import java.time.LocalDateTime
import java.util.UUID

class TasksRepositoryImpl : TasksRepository {
    override suspend fun getTasksByUserId(userId: String): List<Task> {
        return tasksDAO.getTasksByUserId(userId = userId)
    }

    override suspend fun getTasksByUserIdAndPage(userId: String, limit: Int, offset: Int): List<Task> {
        val tasks = tasksDAO.getTasksByUserId(userId = userId)
        return if (offset <= tasks.size) {
            val tasksInPage = tasks.windowed(size = limit, step = offset, partialWindows = true)
            tasksInPage.first()
        } else emptyList()
    }

    override suspend fun insertTask(userId: String, title: String, description: String, category: String): Task? {
        val newTask = Task(
            taskId = UUID.randomUUID().toString(),
            userId = userId,
            title = title,
            description = description,
            category = category,
            state = false,
            startDate = LocalDateTime.now().toString(),
            endDate = LocalDateTime.now().toString()
        )
        return if (tasksDAO.insertTask(task = newTask)) {
            newTask
        } else null
    }

    override suspend fun isTaskIdExist(taskId: String): Boolean {
        return tasksDAO.getTaskByTaskId(taskId = taskId) != null
    }

    override suspend fun updateTaskDescriptionByTaskId(taskId: String, description: String): Task? {
        val success = tasksDAO.updateTaskDescriptionByTaskId(taskId = taskId, description = description)
        return if (success) tasksDAO.getTaskByTaskId(taskId = taskId) else null
    }

    override suspend fun updateTaskStateByTaskId(taskId: String, state: Boolean): Task? {
        val success = tasksDAO.updateTaskStateByTaskId(taskId = taskId, state = state)
        return if (success) tasksDAO.getTaskByTaskId(taskId = taskId) else null
    }

    override suspend fun updateTaskCompletionDateByTaskId(taskId: String, endDate: String): Task? {
        val success = tasksDAO.updateTaskEndDateByTaskId(taskId = taskId, endDate = endDate)
        return if (success) tasksDAO.getTaskByTaskId(taskId = taskId) else null
    }

    override suspend fun deleteTask(taskId: String): Boolean {
        return tasksDAO.deleteTask(taskId = taskId)
    }
}

val tasksRepo : TasksRepository = TasksRepositoryImpl()