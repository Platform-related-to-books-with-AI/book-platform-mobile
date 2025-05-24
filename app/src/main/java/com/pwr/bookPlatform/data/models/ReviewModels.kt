package com.pwr.bookPlatform.data.models

import java.time.ZonedDateTime

enum class ReadingStatus {
    PLAN_TO_READ,
    READING,
    FINISHED,
    DROPPED
}

data class Review(
    val id: Long?,
    val isbn: String,
    val rating: Float,
    val status: ReadingStatus,
    val userId: Long,
    val createdAt: ZonedDateTime?,
    val updatedAt: ZonedDateTime?
)

data class ReviewRequest(
    val isbn: String,
    val rating: Float,
    val status: String,
    val userId: Long
)

data class ReviewResponse(
    val id: Long,
    val isbn: String,
    val rating: Float,
    val status: String,
    val userId: Long,
    val createdAt: String,
    val updatedAt: String
)

data class ReviewsResponse(
    val content: List<ReviewResponse>,
    val totalElements: Int,
    val totalPages: Int,
    val number: Int,
    val size: Int
)
