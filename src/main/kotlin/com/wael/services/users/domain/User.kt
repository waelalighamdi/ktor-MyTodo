package com.wael.services.users.domain

import at.favre.lib.crypto.bcrypt.BCrypt
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: String,
    val email: String,
    val password: String
)

fun toHashPassword(password: String): String {
    return BCrypt.withDefaults().hashToString(12,password.toCharArray())
}

fun toVerifyHashedPassword(password: String, hashedPassword: String): Boolean {
    return BCrypt.verifyer().verify(password.toCharArray(),hashedPassword).verified
}
