package com.pwr.BookPlatform.Data.api

import com.pwr.BookPlatform.Data.Models.BookSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApi {
    @GET("books/")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20,
        @Query("sort") sort: String? = null,
        @Query("page") page: Int = 1
    ): BookSearchResponse
}