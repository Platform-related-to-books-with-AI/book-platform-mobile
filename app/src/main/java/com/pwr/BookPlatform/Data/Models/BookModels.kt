package com.pwr.BookPlatform.Data.Models

data class BookSearchResponse(
    val numFound: Int,
    val start: Int,
    val docs: List<BookDoc>
)

data class BookDoc(
    val key: String,
    val title: String,
    val author_name: List<String>?,
    val first_publish_year: Int?,
    val edition_count: Int?,
    val cover_i: Int?
)
