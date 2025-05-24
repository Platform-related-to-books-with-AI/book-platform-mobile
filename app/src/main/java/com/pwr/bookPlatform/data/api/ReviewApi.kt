package com.pwr.bookPlatform.data.api

import com.pwr.bookPlatform.data.models.ReviewRequest
import com.pwr.bookPlatform.data.models.ReviewResponse
import com.pwr.bookPlatform.data.models.ReviewsResponse
import com.pwr.bookPlatform.data.models.UpdateReviewRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewApi {
    @GET("reviews/user/{userId}")
    suspend fun getUserReviews(
        @Path("userId") userId: Long,
        @Query("page") page: Int = 0,
        @Query("perPage") perPage: Int = 10
    ): ReviewsResponse

    @POST("reviews/")
    suspend fun createReview(
        @Body reviewRequest: ReviewRequest
    ): ReviewResponse

    @DELETE("reviews/{id}")
    suspend fun deleteReview(
        @Path("id") id: Long
    )

    @PATCH("reviews/{id}")
    suspend fun updateReview(
        @Path("id") id: Long,
        @Body updateRequest: UpdateReviewRequest
    ): ReviewResponse
}
