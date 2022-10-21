package com.wael.plugins

import com.wael.services.users.domain.usersRepo
import io.ktor.server.auth.*
import io.ktor.server.application.*

fun Application.configureSecurity() {

    authentication {
        basic(name = "basic_auth") {
            validate { credentials ->
                val user =
                    usersRepo.getUserByEmail(email = credentials.name.lowercase().trim()) ?: return@validate null
                val isPasswordValid =
                    usersRepo.isUserPasswordValid(password = credentials.password, hashedPassword = user.password)

                if (isPasswordValid) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}
