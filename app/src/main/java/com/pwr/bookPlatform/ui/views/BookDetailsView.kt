package com.pwr.bookPlatform.ui.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.pwr.bookPlatform.data.models.BookDetails
import com.pwr.bookPlatform.ui.viewModels.BookDetailsViewModel
import com.gowtham.ratingbar.RatingBar
import com.pwr.bookPlatform.R
import com.pwr.bookPlatform.data.models.ReadingStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsView(
    modifier: Modifier = Modifier,
    bookDetailsViewModel: BookDetailsViewModel,
    bookId: String,
    onBack: () -> Unit
) {
    val bookDetails = bookDetailsViewModel.bookDetails
    var showAddToBookshelfDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler {
        onBack()
    }

    LaunchedEffect(bookId) {
        val workKey = bookId.substringAfterLast('/')
        bookDetailsViewModel.loadBookDetails(workKey)
    }

    bookDetailsViewModel.snackbarMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            bookDetailsViewModel.clearSnackbarMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = bookDetails?.title ?: stringResource(R.string.bookdetails_title),
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.global_back)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (bookDetails != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    BookDetailsContent(
                        book = bookDetails,
                        onAddToBookshelfClick = { showAddToBookshelfDialog = true }
                    )
                }
            }

            if (showAddToBookshelfDialog && bookDetails != null) {
                AddToBookshelfDialog(
                    onDismiss = { showAddToBookshelfDialog = false },
                    onConfirm = { rating, status ->
                        val isbn = bookDetails.key?.substringAfterLast('/') ?: ""
                        bookDetailsViewModel.addToBookshelf(isbn, rating, status)
                        showAddToBookshelfDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun BookDetailsContent(
    book: BookDetails,
    onAddToBookshelfClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            if (!book.covers.isNullOrEmpty() && book.covers[0] > 0) {
                AsyncImage(
                    model = "https://covers.openlibrary.org/b/id/${book.covers[0]}-L.jpg",
                    contentDescription = stringResource(R.string.bookdetails_no_cover),
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                if (!book.authors.isNullOrEmpty()) {
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

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onAddToBookshelfClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.bookdetails_add_to_bookshelf_button))
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

@Composable
fun AddToBookshelfDialog(
    onDismiss: () -> Unit,
    onConfirm: (rating: Float, status: ReadingStatus) -> Unit
) {
    var rating by remember { mutableStateOf(0f) }
    var selectedStatus by remember { mutableStateOf(ReadingStatus.PLAN_TO_READ) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.bookdetails_add_to_bookshelf_title),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(stringResource(R.string.bookdetails_rating),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                RatingBar(
                    value = rating,
                    onValueChange = { rating = it },
                    onRatingChanged = {},
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    stringResource(R.string.bookdetails_status),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                StatusSelector(
                    selectedStatus = selectedStatus,
                    onStatusSelected = { selectedStatus = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text(stringResource(R.string.global_cancel))
                    }

                    Button(
                        onClick = { onConfirm(rating, selectedStatus) }
                    ) {
                        Text(stringResource(R.string.bookdetails_add_to_bookshelf))
                    }
                }
            }
        }
    }
}

@Composable
fun StatusSelector(
    selectedStatus: ReadingStatus,
    onStatusSelected: (ReadingStatus) -> Unit
) {
    val statusOptions = ReadingStatus.values()

    Column {
        statusOptions.forEach { status ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = status == selectedStatus,
                    onClick = { onStatusSelected(status) }
                )
                Text(
                    text = status.toString().replace("_", " "),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
