package com.pwr.bookPlatform.data.models

data class BookReview(
    val review: ReviewResponse,
    val bookDetails: BookDetails? = null
)
