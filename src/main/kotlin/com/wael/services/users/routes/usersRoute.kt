package com.wael.services.users.routes

import com.wael.services.users.domain.usersRepo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.usersRoute() {
    route(path = "/user") {
        //user route message
        get {
            call.respondText(
                text = "User Route for MyTodo List APIs",
                status = HttpStatusCode.OK
            )
        }

        // Register a new user
        post(path = "/register") {
            val userFormParam = call.receiveParameters()

            val email = userFormParam["email"]?.lowercase()?.trim() ?: return@post call.respondText(
                text = "Missing email address",
                status = HttpStatusCode.BadRequest
            )

            val isEmailExist = usersRepo.isUserEmailExist(email)

            if (isEmailExist) return@post call.respondText(
                text = "the email address already used!",
                status = HttpStatusCode.BadRequest
            )

            val password = userFormParam["password"]?.lowercase()?.trim() ?: return@post call.respondText(
                text = "Missing password",
                status = HttpStatusCode.BadRequest
            )

            val user = usersRepo.insertUser(email = email, password = password)

            if (user != null) {
                call.respond(
                    message = user,
                    status = HttpStatusCode.OK
                )
            } else {
                return@post call.respondText(
                    text = "Failed to add the user",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        // Login a user
        authenticate ("basic_auth") {
            post(path = "/login") {
                // Checking Email is responsibility of Basic Authentication
                val email = call.principal<UserIdPrincipal>()?.name!!

                val user = usersRepo.getUserByEmail(email = email)
                    ?: return@post call.respondText(
                        text = "the email address $email is not exist!",
                        status = HttpStatusCode.BadRequest
                    )

                call.respond(
                    message = user.userId,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}