package com.wael.plugins

import com.wael.services.tasks.route.tasksRoute
import com.wael.services.users.routes.usersRoute
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        usersRoute()
        tasksRoute()
    }
}
