package com.pwr.bookPlatform.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr.bookPlatform.data.models.BookDoc
import com.pwr.bookPlatform.data.services.BookService
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BrowserViewModel(private val bookService: BookService) : ViewModel() {

    var books by mutableStateOf<List<BookDoc>>(emptyList())
    var snackbarMessage by mutableStateOf<String?>(null)

    fun search(
        query: String,
        limit: Int = 20,
        sort: String? = "rating desc",
        page: Int = 1
    ) {
        viewModelScope.launch {
            snackbarMessage = null
            bookService.searchBooks(query, limit, sort, page).fold(
                onSuccess = { response ->
                    books = response.docs
                },
                onFailure = { error ->
                    snackbarMessage = when (error) {
                        is HttpException -> "Server error: ${error.code()}"
                        else -> error.localizedMessage ?: "Unknown error"
                    }
                    books = emptyList()
                }
            )
        }
    }

    fun clearSnackbarMessage() {
        snackbarMessage = null
    }
}
