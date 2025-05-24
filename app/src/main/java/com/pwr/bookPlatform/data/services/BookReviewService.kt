package com.pwr.bookPlatform.data.services

import com.pwr.bookPlatform.data.api.BookApi
import com.pwr.bookPlatform.data.api.ReviewApi
import com.pwr.bookPlatform.data.models.BookDetails
import com.pwr.bookPlatform.data.models.BookReview
import com.pwr.bookPlatform.data.models.BookSearchResponse
import com.pwr.bookPlatform.data.models.ReviewRequest
import com.pwr.bookPlatform.data.models.ReviewResponse
import com.pwr.bookPlatform.data.models.ReviewsResponse
import com.pwr.bookPlatform.data.models.UpdateReviewRequest

class BookService(
    private val bookApi: BookApi,
    private val reviewApi: ReviewApi
) {
    suspend fun searchBooks(
        query: String,
        limit: Int = 20,
        sort: String? = "rating desc",
        page: Int = 1
    ): Result<BookSearchResponse> {
        return try {
            val response = bookApi.searchBooks(query, limit, sort, page)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookDetails(workKey: String): Result<BookDetails> {
        return try {
            val result = bookApi.getBookDetails(workKey)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReview(reviewRequest: ReviewRequest): Result<ReviewResponse> {
        return try {
            val response = reviewApi.createReview(reviewRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(id: Long): Result<Unit> {
        return try {
            reviewApi.deleteReview(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserBookReviews(
        userId: Long,
        page: Int = 0,
        perPage: Int = 10
    ): Result<List<BookReview>> {
        return try {
            val reviewsResponse = reviewApi.getUserReviews(userId, page, perPage)
            val bookReviews = mutableListOf<BookReview>()

            for (review in reviewsResponse.content) {
                try {
                    val bookDetails = bookApi.getBookDetails(review.isbn)
                    bookReviews.add(BookReview(review, bookDetails))
                } catch (e: Exception) {
                }
            }

            Result.success(bookReviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateReview(reviewId: Long, rating: Float, status: String, isbn: String): Result<ReviewResponse> {
        return try {
            val userId = com.pwr.bookPlatform.data.session.UserSession.user?.id ?: throw IllegalStateException("User not logged in")

            val updateRequest = UpdateReviewRequest(isbn, rating, status, userId)
            val response = reviewApi.updateReview(reviewId, updateRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

