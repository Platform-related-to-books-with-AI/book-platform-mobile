package com.pwr.bookPlatform.data.api

import com.pwr.bookPlatform.data.models.Post
import com.pwr.bookPlatform.data.models.PostRequest
import com.pwr.bookPlatform.data.models.PostsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApi {
    @GET("posts/{nickname}")
    suspend fun getUserPosts(
        @Header("Authorization") token: String,
        @Path("nickname") nickname: String,
        @Query("page") page: Int = 0,
        @Query("perPage") perPage: Int = 10
    ): PostsResponse

    @POST("posts/")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Body postRequest: PostRequest
    ): Post
}
