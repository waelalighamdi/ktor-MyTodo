package com.wael.services.users.data

import com.wael.database.DatabaseFactory.dbQuery
import com.wael.services.users.domain.User
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UsersDAOImpl : UsersDAO {
    override suspend fun insertUser(user: User): Boolean {
        val queryResult = dbQuery {
            val insertStatement = Users.insert { users ->
                users[userId] = user.userId
                users[email] = user.email
                users[password] = user.password
            }
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
        }
        return queryResult != null
    }

    override suspend fun getUserByEmail(email: String): User? {
        return dbQuery {
            Users.select {
                Users.email eq email
            }.map(::resultRowToUser).singleOrNull()
        }
    }

    override suspend fun getUserByUserID(userId: String): User? {
        return dbQuery {
            Users.select {
                Users.userId eq userId
            }.map(::resultRowToUser).singleOrNull()
        }
    }
}

val usersDAO: UsersDAO = UsersDAOImpl()