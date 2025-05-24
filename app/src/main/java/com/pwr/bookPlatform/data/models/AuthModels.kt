package com.pwr.bookPlatform.data.models

data class AuthResponse(
    val user: UserResponse,
    val token: String
)

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String,
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
    val username: String,
    val password: String
)