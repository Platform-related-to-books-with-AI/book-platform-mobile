package com.pwr.bookPlatform.data.api

import com.pwr.bookPlatform.data.models.LikeResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface LikeApi {
    @GET("likes/{likeTargetId}")
    suspend fun toggleLike(
        @Header("Authorization") token: String,
        @Path("likeTargetId") likeTargetId: Long
    ): LikeResponse
}
