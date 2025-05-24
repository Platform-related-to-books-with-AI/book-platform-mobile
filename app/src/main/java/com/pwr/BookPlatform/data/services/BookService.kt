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
    ): Result<BookSearchResponse> {
        return try{
            val response = api.searchBooks(query, limit, sort, page)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookDetails(workKey: String): Result<BookDetails> {
        return try {
            val result = api.getBookDetails(workKey)
            Result.success(result)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}

