package com.wael.services.users.data

import com.wael.services.users.domain.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Users: Table() {
    val userId = varchar("userId", 64)
    val email = varchar("email", 128)
    val password = varchar("password", 128)

    override val primaryKey = PrimaryKey(userId)
}

// this is a mapper function
fun resultRowToUser(row: ResultRow) = User(
    userId = row[Users.userId],
    email = row[Users.email],
    password = row[Users.password]
)
