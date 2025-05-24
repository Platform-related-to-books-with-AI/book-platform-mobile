package com.pwr.BookPlatform.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr.BookPlatform.data.models.BookDetails
import com.pwr.BookPlatform.data.models.LoginRequest
import com.pwr.BookPlatform.data.models.ReadingStatus
import com.pwr.BookPlatform.data.models.ReviewRequest
import com.pwr.BookPlatform.data.services.BookService
import com.pwr.BookPlatform.data.services.ReviewService
import com.pwr.BookPlatform.data.session.UserSession
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BookDetailsViewModel(
    private val bookService: BookService,
    private val reviewService: ReviewService
) : ViewModel() {
    var bookDetails by mutableStateOf<BookDetails?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var addToBookshelfStatus by mutableStateOf<String?>(null)

    fun loadBookDetails(workKey: String) {
        viewModelScope.launch {
            errorMessage = null
            val result = bookService.getBookDetails(workKey)
            result
                .onSuccess { details ->
                    bookDetails = details
                }
                .onFailure { error ->
                    when (error) {
                        is HttpException -> {
                            errorMessage = "Server error: ${error.code()}"
                        }

                        else -> {
                            errorMessage = error.localizedMessage ?: "Unknown error"
                        }
                    }
                }
        }
    }

    fun addToBookshelf(isbn: String, rating: Float, status: ReadingStatus) {
        val userId = UserSession.user?.id ?: return
        viewModelScope.launch {
            val request = ReviewRequest(
                isbn = isbn,
                rating = rating,
                status = status.toString(),
                userId = userId
            )
            val result = reviewService.createReview(request)
            result
                .onSuccess {
                    addToBookshelfStatus = "Book added to bookshelf"
                }
                .onFailure { error ->
                    addToBookshelfStatus = when (error) {
                        is HttpException -> "Server error: ${error.code()}"
                        else -> error.localizedMessage ?: "Unknown error"
                    }
                }
        }
    }
}

