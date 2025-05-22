package com.pwr.BookPlatform.Data.Services
import com.pwr.BookPlatform.Data.Models.BookSearchResponse
import com.pwr.BookPlatform.Data.api.BookApi

class BookService(private val api: BookApi) {

    suspend fun searchBooks(
        query: String,
        limit: Int = 20,
        sort: String? = "rating desc",
        page: Int = 1
    ): BookSearchResponse {
        return api.searchBooks(query, limit, sort, page)
    }
}