package com.pwr.bookPlatform.data.api

import com.pwr.bookPlatform.data.models.BookSearchResponse
import com.pwr.bookPlatform.data.models.BookDetails
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface BookApi {
    @GET("books/")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20,
        @Query("sort") sort: String? = null,
        @Query("page") page: Int = 1
    ): BookSearchResponse

    @GET("books/works/{workKey}")
    suspend fun getBookDetails(
        @Path("workKey") workKey: String
    ): BookDetails
}

