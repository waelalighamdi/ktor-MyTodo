package com.wael.services.users.domain

interface UsersRepository {
    suspend fun insertUser(email: String, password: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun isUserEmailExist(email: String): Boolean
    suspend fun isUserPasswordValid(password: String, hashedPassword: String): Boolean
    suspend fun isUserIdExist(userId: String): Boolean
}