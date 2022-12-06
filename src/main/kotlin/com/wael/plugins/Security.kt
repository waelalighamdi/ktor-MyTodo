package com.wael.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.wael.services.users.domain.userJWTConfig
import com.wael.services.users.domain.usersRepo
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

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
        jwt(name = "jwt_auth") {
            realm = userJWTConfig.REALM
            verifier(JWT
                .require(Algorithm.HMAC256(userJWTConfig.SECRET))
                .withAudience(userJWTConfig.AUDIENCE)
                .withIssuer(userJWTConfig.ISSUER)
                .build())
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asString()
                val isUserIdExist = usersRepo.isUserIdExist(userId = userId)
                if (isUserIdExist) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
