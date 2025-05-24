package com.pwr.BookPlatform.data.services

import com.pwr.BookPlatform.data.api.ReviewApi
import com.pwr.BookPlatform.data.models.ReviewRequest
import com.pwr.BookPlatform.data.models.ReviewResponse
import com.pwr.BookPlatform.data.models.ReviewsResponse

class ReviewService(private val api: ReviewApi) {

    suspend fun getUserReviews(
        userId: Long,
        page: Int = 0,
        perPage: Int = 10
    ): Result<ReviewsResponse> {
        return try {
            val result = api.getUserReviews(userId, page, perPage)
            Result.success(result)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReview(reviewRequest: ReviewRequest): Result<ReviewResponse> {
        return try {
            val response = api.createReview(reviewRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(id: Long) : Result<Unit> {
        return try {
            api.deleteReview(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
