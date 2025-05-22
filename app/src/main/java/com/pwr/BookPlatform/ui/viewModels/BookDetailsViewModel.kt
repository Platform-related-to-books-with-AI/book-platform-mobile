package com.pwr.BookPlatform.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr.BookPlatform.data.models.BookDetails
import com.pwr.BookPlatform.data.services.BookService
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BookDetailsViewModel(private val bookService: BookService) : ViewModel() {
    var bookDetails by mutableStateOf<BookDetails?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadBookDetails(workKey: String) {
        viewModelScope.launch {
            errorMessage = null
            try {
                bookDetails = bookService.getBookDetails(workKey)
            } catch (e: Exception) {
                errorMessage = when (e) {
                    is HttpException -> "Server error: ${e.code()}"
                    else -> e.localizedMessage ?: "Unknown error"
                }
                bookDetails = null
            }
        }
    }
}

