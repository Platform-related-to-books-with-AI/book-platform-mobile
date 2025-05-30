package com.pwr.bookPlatform.data.services

import com.pwr.bookPlatform.data.api.LikeApi
import com.pwr.bookPlatform.data.api.RetrofitInstance
import com.pwr.bookPlatform.data.models.LikeResponse
import com.pwr.bookPlatform.data.session.UserSession

object LikeService {
    private val likeApi: LikeApi by lazy { RetrofitInstance.likeApi }

    suspend fun toggleLike(likeTargetId: Long): Result<LikeResponse> {
        return try {
            val token = UserSession.token ?: return Result.failure(Exception("Not authenticated"))
            val response = likeApi.toggleLike("Bearer $token", likeTargetId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
