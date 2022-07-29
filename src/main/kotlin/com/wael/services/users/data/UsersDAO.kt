package com.wael.services.users.data

import com.wael.services.users.domain.User

interface UsersDAO {
    suspend fun insertUser(user:User): Boolean
    suspend fun getUserByEmail(email: String): User?
    suspend fun getUserByUserID(userId: String): User?
}