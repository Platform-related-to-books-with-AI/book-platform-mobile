package com.pwr.bookPlatform.data.models

data class AuthResponse(
    val user: UserResponse,
    val token: String
)

data class UserResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val role: String,
    val active: Boolean,
    val createdAt: String,
    val updatedAt: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val nickname: String,
    val password: String
)

data class UpdateUserRequest(
    val email: String,
    val nickname: String,
    val password: String? = null
)
