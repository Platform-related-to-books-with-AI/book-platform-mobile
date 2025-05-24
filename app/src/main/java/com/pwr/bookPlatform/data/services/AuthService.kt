package com.pwr.bookPlatform.data.services

import com.pwr.bookPlatform.data.models.AuthResponse
import com.pwr.bookPlatform.data.models.LoginRequest
import com.pwr.bookPlatform.data.models.RegisterRequest
import com.pwr.bookPlatform.data.api.AuthApi

class AuthService(private val api: AuthApi) {

    suspend fun sendLoginRequest(loginRequest: LoginRequest): Result<AuthResponse> {
        return try {
            val response = api.login(loginRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendRegisterRequest(registerRequest: RegisterRequest): Result<AuthResponse> {
        return try {
            val response = api.register(registerRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(token: String): Result<AuthResponse> {
        return try {
            val response = api.getMe("Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
