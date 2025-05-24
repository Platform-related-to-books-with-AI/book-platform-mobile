package com.pwr.bookPlatform.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr.bookPlatform.data.models.BookReview
import com.pwr.bookPlatform.data.services.BookService
import com.pwr.bookPlatform.data.session.UserSession
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BookshelfViewModel(
    private val bookService: BookService
) : ViewModel() {

    var bookReviews by mutableStateOf<List<BookReview>>(emptyList())
    var errorMessage by mutableStateOf<String?>(null)

    init {
        loadUserReviews()
    }

    fun loadUserReviews() {
        viewModelScope.launch {
            errorMessage = null
            bookReviews = emptyList()

            val userId = UserSession.user?.id ?: run {
                errorMessage = "User is not logged in"
                return@launch
            }

            bookService.getUserBookReviews(userId).fold(
                onSuccess = { reviews ->
                    bookReviews = reviews
                    errorMessage = null
                },
                onFailure = { exception ->
                    errorMessage = when (exception) {
                        is HttpException -> "Server error: ${exception.code()}"
                        else -> exception.localizedMessage ?: "Unknown error"
                    }
                }
            )
        }
    }

    fun deleteReview(reviewId: Long) {
        viewModelScope.launch {
            bookService.deleteReview(reviewId).fold(
                onSuccess = {
                    loadUserReviews()
                },
                onFailure = { exception ->
                    errorMessage = when (exception) {
                        is HttpException -> "Error deleting review: ${exception.code()}"
                        else -> exception.localizedMessage ?: "Unknown error"
                    }
                }
            )
        }
    }
}

