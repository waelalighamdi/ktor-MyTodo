package com.wael.services.users.domain

import com.wael.services.users.data.usersDAO
import java.util.UUID

class UsersRepositoryImpl : UsersRepository {
    override suspend fun insertUser(email: String, password: String): User? {
        val newUser = User(
            userId = UUID.randomUUID().toString(),
            email = email,
            password = toHashPassword(password = password)
        )
        return if (usersDAO.insertUser(newUser)) {
            newUser
        } else null
    }

    override suspend fun getUserByEmail(email: String): User? {
        return usersDAO.getUserByEmail(email = email)
    }

    override suspend fun isUserEmailExist(email: String): Boolean {
        return usersDAO.getUserByEmail(email = email) != null
    }

    override suspend fun isUserPasswordValid(password: String, hashedPassword: String): Boolean {
        return toVerifyHashedPassword(password = password, hashedPassword = hashedPassword)
    }

    override suspend fun isUserIdExist(userId: String): Boolean {
        return usersDAO.getUserByUserID(userId = userId) != null
    }
}

val usersRepo : UsersRepository = UsersRepositoryImpl()