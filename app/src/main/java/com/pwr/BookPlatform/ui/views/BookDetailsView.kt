package com.pwr.BookPlatform.ui.views

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.pwr.BookPlatform.data.models.BookDetails
import com.pwr.BookPlatform.R
import com.pwr.BookPlatform.ui.viewModels.BookDetailsViewModel
import com.gowtham.ratingbar.RatingBar

@Composable
fun BookDetailsView(
    modifier: Modifier = Modifier,
    bookDetailsViewModel: BookDetailsViewModel,
    bookId: String,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }
    LaunchedEffect(bookId) {
        val workKey = bookId.substringAfterLast('/')
        bookDetailsViewModel.loadBookDetails(workKey)
    }
    val bookDetails = bookDetailsViewModel.bookDetails
    val errorMessage = bookDetailsViewModel.errorMessage


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        when {
            errorMessage != null -> {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            bookDetails != null -> {
                BookDetailsContent(bookDetails)
            }
        }
    }
}

@Composable
fun BookDetailsContent(book: BookDetails) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            if (!book.covers.isNullOrEmpty() && book.covers[0] > 0) {
                AsyncImage(
                    model = "https://covers.openlibrary.org/b/id/${book.covers[0]}-L.jpg",
                    contentDescription = null,
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(end = 20.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title ?: stringResource(R.string.bookdetails_no_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                if (!book.authors.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.bookdetails_authors),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = book.authors.filter { !it.author.isNullOrBlank() }.joinToString { it.author ?: "" },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (book.ratings_average != null && book.ratings_average > 0) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        RatingBar(
                            value = book.ratings_average,
                            onValueChange = {},
                            onRatingChanged = {},
                            modifier = Modifier.height(30.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = String.format(
                                "%.1f",
                                book.ratings_average
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        if (!book.subject_people.isNullOrEmpty()) {
            val peopleToShow = book.subject_people.take(10)
            Text(
                text = stringResource(R.string.bookdetails_people),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = peopleToShow.joinToString(),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        if (!book.subject_places.isNullOrEmpty()) {
            val placesToShow = book.subject_places.take(10)
            Text(
                text = stringResource(R.string.bookdetails_places),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = placesToShow.joinToString(),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        if (!book.subject_times.isNullOrEmpty()) {
            val timesToShow = book.subject_times.take(10)
            Text(
                text = stringResource(R.string.bookdetails_times),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = timesToShow.joinToString(),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        if (!book.subjects.isNullOrEmpty()) {
            val subjectsToShow = book.subjects.take(10)
            Text(
                text = stringResource(R.string.bookdetails_subjects),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subjectsToShow.joinToString(),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        if (book.description != null) {
            val desc = when (book.description) {
                is String -> book.description as String
                is Map<*, *> -> book.description?.let { (it as? Map<*, *>)?.get("value") as? String } ?: ""
                else -> ""
            }
            if (desc.isNotBlank()) {
                Text(
                    text = stringResource(R.string.bookdetails_description),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
