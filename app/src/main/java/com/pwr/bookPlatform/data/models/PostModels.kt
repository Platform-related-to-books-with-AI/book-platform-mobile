package com.pwr.bookPlatform.data.models

import java.time.ZonedDateTime

data class Post(
    val id: Long,
    val text: String,
    val likes: Int,
    val createdAt: String,
    val updatedAt: String
)

data class PostRequest(
    val text: String,
    val nickname: String
)

data class PostsResponse(
    val content: List<Post>,
    val totalElements: Int,
    val totalPages: Int,
    val number: Int,
    val size: Int
)
