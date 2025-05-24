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
    var addToBookshelfStatus by mutableStateOf<String>("")

    fun loadBookDetails(workKey: String) {
        viewModelScope.launch {
            errorMessage = null
            val result = bookService.getBookDetails(workKey)
            result.onSuccess { details ->
                bookDetails = details

            }.onFailure { error ->
                errorMessage = when (error) {
                    is HttpException -> "Server error: ${error.code()}"
                    else -> error.localizedMessage ?: "Unknown error"
                }
                bookDetails = null
            }
        }
    }

    fun addToBookshelf(isbn: String, rating: Float, status: ReadingStatus) {
        val userId = UserSession.user?.id ?: return
        addToBookshelfStatus = ""
        viewModelScope.launch {
            val request = ReviewRequest(
                isbn = isbn,
                rating = rating,
                status = status.toString(),
                userId = userId
            )
            reviewService.createReview(request)
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

