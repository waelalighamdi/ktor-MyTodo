package com.wael.services.tasks.route

import com.wael.services.tasks.domain.tasksRepo
import com.wael.services.users.domain.usersRepo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.tasksRoute() {
    authenticate("jwt_auth") {
        route(path = "/task") {
            // the task home message
//            get {
//                call.respondText(
//                    text = "Tasks Route",
//                    status = HttpStatusCode.OK
//                )
//            }
            // get tasks for a specific user
//            get(path = "{userId}") {
//                val userId = call.parameters["userId"] ?: return@get call.respondText(
//                    text = "Missing the User Id",
//                    status = HttpStatusCode.BadRequest
//                )
//
//                val tasks = tasksRepo.getTasksByUserId(userId = userId)
//
//                call.respond(
//                    message = tasks,
//                    status = HttpStatusCode.OK
//                )
//            }
//            http://0.0.0.0:8080/task?userId=xxxxx&limit=y&offset=z
            get {
                val principal = call.principal<JWTPrincipal>()
                // confirming the userID existence is responsibility of JWT authentication
                val userId =
                    principal!!.payload.getClaim("userId").toString().removeSurrounding(prefix = "\"", suffix = "\"")

                val limit = call.request.queryParameters["limit"]?.toInt() ?: 5
                val offset = call.request.queryParameters["offset"]?.toInt() ?: 1

                val tasks = tasksRepo.getTasksByUserIdAndPage(userId = userId, limit = limit, offset = offset)

                call.respond(
                    message = tasks,
                    status = HttpStatusCode.OK
                )
            }

            // insert a new task
            post {
                val taskFormParam = call.receiveParameters()

                val principal = call.principal<JWTPrincipal>()
                // confirming the userID existence is responsibility of JWT authentication
                val userId =
                    principal!!.payload.getClaim("userId").toString().removeSurrounding(prefix = "\"", suffix = "\"")

                // check if the user id is existing in the Database
                val isUserIdExist = usersRepo.isUserIdExist(userId = userId)

                if (!isUserIdExist) return@post call.respondText(
                    text = "The User Id: $userId is incorrect!",
                    status = HttpStatusCode.NotAcceptable
                )

                val title = taskFormParam["title"] ?: return@post call.respondText(
                    text = "Missing the Task Title",
                    status = HttpStatusCode.BadRequest
                )

                val description = taskFormParam["description"] ?: return@post call.respondText(
                    text = "Missing the Task Description",
                    status = HttpStatusCode.BadRequest
                )

                val category = taskFormParam["category"] ?: return@post call.respondText(
                    text = "Missing the Task Category",
                    status = HttpStatusCode.BadRequest
                )

                val newTask = tasksRepo.insertTask(
                    userId = userId,
                    title = title,
                    description = description,
                    category = category
                )

                if (newTask != null) {
                    call.respond(
                        message = newTask,
                        status = HttpStatusCode.OK
                    )
                } else {
                    call.respondText(
                        text = "Failed to insert a new task",
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }

            // update the description for a specific task
            patch(path = "/description") {
                val taskFormParam = call.receiveParameters()

                val taskId = taskFormParam["taskId"] ?: return@patch call.respondText(
                    text = "Missing the Task Id",
                    status = HttpStatusCode.BadRequest
                )

                // check if the task id is existing in the Database
                val isTaskIdExist = tasksRepo.isTaskIdExist(taskId = taskId)

                if (!isTaskIdExist) return@patch call.respondText(
                    text = "The Task Id: $taskId is incorrect!",
                    status = HttpStatusCode.NotAcceptable
                )

                val description = taskFormParam["description"] ?: return@patch call.respondText(
                    text = "Missing the Task Description",
                    status = HttpStatusCode.BadRequest
                )

                val task = tasksRepo.updateTaskDescriptionByTaskId(
                    taskId = taskId,
                    description = description
                )

                if (task != null) {
                    call.respond(
                        message = task,
                        status = HttpStatusCode.OK
                    )
                } else {
                    call.respondText(
                        text = "Failed to update the task",
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }

            // update the state for a specific task
            patch(path = "/state") {
                val taskFormParam = call.receiveParameters()

                val taskId = taskFormParam["taskId"] ?: return@patch call.respondText(
                    text = "Missing the Task Id",
                    status = HttpStatusCode.BadRequest
                )

                // check if the task id is existing in the Database
                val isTaskIdExist = tasksRepo.isTaskIdExist(taskId = taskId)

                if (!isTaskIdExist) return@patch call.respondText(
                    text = "The Task Id: $taskId is incorrect!",
                    status = HttpStatusCode.NotAcceptable
                )

                val state = taskFormParam["state"] ?: return@patch call.respondText(
                    text = "Missing the Task State",
                    status = HttpStatusCode.BadRequest
                )

                val task = tasksRepo.updateTaskStateByTaskId(
                    taskId = taskId,
                    state = state.toBoolean()
                )

                if (task != null) {
                    call.respond(
                        message = task,
                        status = HttpStatusCode.OK
                    )
                } else {
                    call.respondText(
                        text = "Failed to update the task",
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }

            // update the completion date for a specific task
            patch(path = "/completion") {
                val taskFormParam = call.receiveParameters()

                val taskId = taskFormParam["taskId"] ?: return@patch call.respondText(
                    text = "Missing the Task Id",
                    status = HttpStatusCode.BadRequest
                )

                // check if the task id is existing in the Database
                val isTaskIdExist = tasksRepo.isTaskIdExist(taskId = taskId)

                if (!isTaskIdExist) return@patch call.respondText(
                    text = "The Task Id: $taskId is incorrect!",
                    status = HttpStatusCode.NotAcceptable
                )

                val endDate = taskFormParam["endDate"] ?: return@patch call.respondText(
                    text = "Missing the Task Completion Date",
                    status = HttpStatusCode.BadRequest
                )

                val task = tasksRepo.updateTaskCompletionDateByTaskId(
                    taskId = taskId,
                    endDate = endDate
                )

                if (task != null) {
                    call.respond(
                        message = task,
                        status = HttpStatusCode.OK
                    )
                } else {
                    call.respondText(
                        text = "Failed to update the task",
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }

            // delete a specific task
            delete {
                val taskFormParam = call.receiveParameters()

                val taskId = taskFormParam["taskId"] ?: return@delete call.respondText(
                    text = "Missing the Task Id",
                    status = HttpStatusCode.BadRequest
                )

                // check if the task id is existing in the Database
                val isTaskIdExist = tasksRepo.isTaskIdExist(taskId = taskId)

                if (!isTaskIdExist) return@delete call.respondText(
                    text = "The Task Id: $taskId is incorrect!",
                    status = HttpStatusCode.NotAcceptable
                )

                val task = tasksRepo.deleteTask(taskId = taskId)

                if (task) {
                    call.respond(
                        message = "Task deleted successfully",
                        status = HttpStatusCode.OK
                    )
                } else {
                    call.respondText(
                        text = "Failed to delete the task",
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
        }
    }
}