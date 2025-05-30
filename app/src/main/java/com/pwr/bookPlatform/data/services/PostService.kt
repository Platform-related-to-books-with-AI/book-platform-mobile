package com.pwr.bookPlatform.data.services

import com.pwr.bookPlatform.data.api.PostApi
import com.pwr.bookPlatform.data.api.RetrofitInstance
import com.pwr.bookPlatform.data.models.Post
import com.pwr.bookPlatform.data.models.PostRequest
import com.pwr.bookPlatform.data.models.PostsResponse
import com.pwr.bookPlatform.data.session.UserSession

object PostService {
    private val postApi: PostApi by lazy { RetrofitInstance.postApi }

    suspend fun getUserPosts(
        nickname: String,
        page: Int = 0,
        perPage: Int = 10
    ): Result<PostsResponse> {
        return try {
            val token = UserSession.token ?: return Result.failure(Exception("Not authenticated"))
            val result = postApi.getUserPosts("Bearer $token", nickname, page, perPage)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPost(text: String, nickname: String): Result<Post> {
        return try {
            val token = UserSession.token ?: return Result.failure(Exception("Not authenticated"))
            val postRequest = PostRequest(text, nickname)
            val response = postApi.createPost("Bearer $token", postRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
