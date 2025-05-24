package com.pwr.BookPlatform.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr.BookPlatform.data.models.BookDoc
import com.pwr.BookPlatform.data.services.BookService
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BrowserViewModel(private val bookService: BookService) : ViewModel() {

    var books by mutableStateOf<List<BookDoc>>(emptyList())
    var errorMessage by mutableStateOf<String?>(null)

    fun search(
        query: String,
        limit: Int = 20,
        sort: String? = "rating desc",
        page: Int = 1
    ) {
        viewModelScope.launch {
            errorMessage = null
            val result = bookService.searchBooks(query, limit, sort, page)

            result
            .onSuccess { response ->
                books = response.docs
            }
            .onFailure { error ->
                errorMessage = when (error) {
                    is HttpException -> "Server error: ${error.code()}"
                    else -> error.localizedMessage ?: "Unknown error"
                }
                books = emptyList()
            }
        }
    }
}
