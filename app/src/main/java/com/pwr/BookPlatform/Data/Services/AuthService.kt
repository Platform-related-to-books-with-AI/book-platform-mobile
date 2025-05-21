package com.pwr.BookPlatform.Data.Services

import com.pwr.BookPlatform.Data.Models.AuthResponse
import com.pwr.BookPlatform.Data.Models.LoginRequest
import com.pwr.BookPlatform.Data.Models.RegisterRequest
import com.pwr.BookPlatform.Data.api.AuthApi

import okhttp3.ResponseBody
import retrofit2.HttpException
import org.json.JSONObject

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
