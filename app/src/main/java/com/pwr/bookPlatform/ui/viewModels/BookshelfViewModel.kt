package com.pwr.bookPlatform.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr.bookPlatform.data.models.BookReview
import com.pwr.bookPlatform.data.services.BookService
import com.pwr.bookPlatform.data.session.UserSession
import com.pwr.bookPlatform.data.models.ReadingStatus
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BookshelfViewModel(
    private val bookService: BookService
) : ViewModel() {

    var bookReviews by mutableStateOf<List<BookReview>>(emptyList())
    var snackbarMessage by mutableStateOf<String?>(null)

    init {
        loadUserReviews()
    }

    fun loadUserReviews() {
        viewModelScope.launch {
            snackbarMessage = null

            val userId = UserSession.user?.id ?: run {
                snackbarMessage = "User is not logged in"
                return@launch
            }

            bookService.getUserBookReviews(userId).fold(
                onSuccess = { reviews ->
                    bookReviews = reviews
                },
                onFailure = { exception ->
                    snackbarMessage = when (exception) {
                        is HttpException -> "Server error: ${exception.code()}"
                        else -> exception.localizedMessage ?: "Unknown error"
                    }
                }
            )
        }
    }

    fun deleteReview(reviewId: Long) {
        viewModelScope.launch {
            snackbarMessage = null

            bookService.deleteReview(reviewId).fold(
                onSuccess = {
                    bookReviews = bookReviews.filter { it.review.id != reviewId }
                    snackbarMessage = "Review successfully deleted"
                },
                onFailure = { exception ->
                    snackbarMessage = when (exception) {
                        is HttpException -> "Error deleting review: ${exception.code()}"
                        else -> exception.localizedMessage ?: "Unknown error"
                    }
                }
            )
        }
    }

    fun updateReview(review: BookReview, newRating: Float, newStatus: ReadingStatus) {
        viewModelScope.launch {
            snackbarMessage = null

            val userId = UserSession.user?.id ?: run {
                snackbarMessage = "User is not logged in"
                return@launch
            }

            bookService.updateReview(review.review.id, newRating, newStatus.toString(), review.review.isbn).fold(
                onSuccess = { updatedReview ->
                    bookReviews = bookReviews.map { bookReview ->
                        if (bookReview.review.id == review.review.id) {
                            bookReview.copy(review = updatedReview)
                        } else {
                            bookReview
                        }
                    }
                    snackbarMessage = "Review successfully updated"
                },
                onFailure = { exception ->
                    snackbarMessage = when (exception) {
                        is HttpException -> "Error updating review: ${exception.code()}"
                        else -> exception.localizedMessage ?: "Unknown error"
                    }
                }
            )
        }
    }

    fun clearSnackbarMessage() {
        snackbarMessage = null
    }
}



