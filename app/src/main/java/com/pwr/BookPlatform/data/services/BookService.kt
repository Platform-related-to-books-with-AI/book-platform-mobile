package com.pwr.BookPlatform.data.services
import com.pwr.BookPlatform.data.models.BookSearchResponse
import com.pwr.BookPlatform.data.models.BookDetails
import com.pwr.BookPlatform.data.api.BookApi

class BookService(private val api: BookApi) {

    suspend fun searchBooks(
        query: String,
        limit: Int = 20,
        sort: String? = "rating desc",
        page: Int = 1
    ): BookSearchResponse {
        return api.searchBooks(query, limit, sort, page)
    }

    suspend fun getBookDetails(workKey: String): BookDetails {
        return api.getBookDetails(workKey)
    }
}

