package com.pwr.bookPlatform.data.services

import com.pwr.bookPlatform.data.api.PostApi
import com.pwr.bookPlatform.data.api.RetrofitInstance
import com.pwr.bookPlatform.data.models.Post
import com.pwr.bookPlatform.data.models.PostRequest
import com.pwr.bookPlatform.data.models.PostsResponse

object PostService {
    private val postApi: PostApi by lazy { RetrofitInstance.postApi }

    suspend fun getUserPosts(
        nickname: String,
        page: Int = 0,
        perPage: Int = 10
    ): Result<PostsResponse> {
        return try {
            val result = postApi.getUserPosts(nickname, page, perPage)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPost(text: String, nickname: String): Result<Post> {
        return try {
            val postRequest = PostRequest(text, nickname)
            val response = postApi.createPost(postRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
