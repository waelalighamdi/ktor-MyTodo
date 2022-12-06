package com.wael.services.users.domain

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*


object UserJWTConfig {
    val SECRET = System.getenv("myTodo_secret") ?: "mysecret"
    const val ISSUER = "http://0.0.0.0:8090/"
    const val AUDIENCE = "http://0.0.0.0:8090/task"
    const val REALM = "Access to mytodo task resource"
    const val REFRESH_TOKEN_LIFETIME = (30L * 24L * 60L * 60L * 1000L) // almost a month
    const val ACCESS_TOKEN_LIFETIME = (60L * 60L * 1000L) // an hour
}

fun generateToken(userId: String, tokenLifetime: Long): String =
    JWT.create()
        .withAudience(userJWTConfig.AUDIENCE)
        .withIssuer(userJWTConfig.ISSUER)
        .withClaim("userId",userId)
        .withExpiresAt(Date(System.currentTimeMillis() + tokenLifetime))
        .sign(Algorithm.HMAC256(userJWTConfig.SECRET))

val userJWTConfig = UserJWTConfig