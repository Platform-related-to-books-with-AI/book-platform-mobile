package com.pwr.BookPlatform.data.api

import com.pwr.BookPlatform.data.models.BookSearchResponse
import com.pwr.BookPlatform.data.models.BookDetails
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

