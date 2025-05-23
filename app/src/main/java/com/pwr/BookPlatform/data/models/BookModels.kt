package com.pwr.BookPlatform.data.models

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
    val cover_i: Int?,
    val ratings_average: Float?
)

data class BookDetails(
    val title: String?,
    val key: String?,
    val description: Any?,
    val covers: List<Int>?,
    val subjects: List<String>?,
    val subject_places: List<String>?,
    val subject_people: List<String>?,
    val subject_times: List<String>?,
    val authors: List<AuthorSimple>?,
    val ratings_average: Float?
)

data class AuthorSimple(
    val author: String?
)
