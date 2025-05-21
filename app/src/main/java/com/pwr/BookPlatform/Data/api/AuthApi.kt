package com.pwr.BookPlatform.Data.api

import com.pwr.BookPlatform.Data.Models.AuthResponse
import com.pwr.BookPlatform.Data.Models.LoginRequest
import com.pwr.BookPlatform.Data.Models.RegisterRequest

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
