package com.pwr.bookPlatform.data.api

import com.pwr.bookPlatform.data.models.AuthResponse
import com.pwr.bookPlatform.data.models.LoginRequest
import com.pwr.bookPlatform.data.models.RegisterRequest

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse

    @GET("auth/me")
    suspend fun getMe(@Header("Authorization") token: String): AuthResponse
}
