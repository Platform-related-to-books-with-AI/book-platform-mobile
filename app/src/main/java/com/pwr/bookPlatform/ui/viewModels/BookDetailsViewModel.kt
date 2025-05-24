package com.pwr.bookPlatform.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr.bookPlatform.data.models.BookDetails
import com.pwr.bookPlatform.data.models.ReadingStatus
import com.pwr.bookPlatform.data.models.ReviewRequest
import com.pwr.bookPlatform.data.services.BookService
import com.pwr.bookPlatform.data.session.UserSession
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BookDetailsViewModel(
    private val bookService: BookService
) : ViewModel() {
    var bookDetails by mutableStateOf<BookDetails?>(null)
    var snackbarMessage by mutableStateOf<String?>(null)

    fun loadBookDetails(workKey: String) {
        viewModelScope.launch {
            bookService.getBookDetails(workKey).fold(
                onSuccess = { details ->
                    bookDetails = details
                },
                onFailure = { error ->
                    snackbarMessage = when (error) {
                        is HttpException -> "Server error: ${error.code()}"
                        else -> error.localizedMessage ?: "Unknown error"
                    }
                }
            )
        }
    }

    fun addToBookshelf(isbn: String, rating: Float, status: ReadingStatus) {
        val userId = UserSession.user?.id ?: run {
            snackbarMessage = "User is not logged in"
            return
        }
        viewModelScope.launch {
            val request = ReviewRequest(
                isbn = isbn,
                rating = rating,
                status = status.toString(),
                userId = userId
            )
            bookService.createReview(request).fold(
                onSuccess = {
                    snackbarMessage = "Book added to bookshelf"
                },
                onFailure = { error ->
                    snackbarMessage = when (error) {
                        is HttpException -> "Server error: ${error.code()}"
                        else -> error.localizedMessage ?: "Unknown error"
                    }
                }
            )
        }
    }

    fun clearSnackbarMessage() {
        snackbarMessage = null
    }
}

