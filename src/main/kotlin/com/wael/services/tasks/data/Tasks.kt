package com.wael.services.tasks.data

import com.wael.services.tasks.domain.Task
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Tasks: Table() {
    val taskId = varchar("taskId", 64)
    val userId = varchar("userId", 64)
    val title = varchar("title", 128)
    val description = varchar("description", 512)
    val category = varchar("category", 64)
    val state = bool("state")
    val startDate = datetime("start_date")
    val endDate = datetime("end_date")

    override val primaryKey = PrimaryKey(taskId)
}

// this is a mapper function that builds a task data class
fun resultRowToTask(row: ResultRow) = Task(
    taskId = row[Tasks.taskId],
    userId = row[Tasks.userId],
    title = row[Tasks.title],
    description = row[Tasks.description],
    category = row[Tasks.category],
    state = row[Tasks.state],
    startDate = row[Tasks.startDate].toString(),
    endDate = row[Tasks.endDate].toString(),
)

// this is a helper function that parse local date time alike string
// and return LocalDateTime object
fun toLocalDateTime(localDateTime: String): LocalDateTime {
    return LocalDateTime.parse(localDateTime)
}