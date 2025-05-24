package com.pwr.bookPlatform.ui.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pwr.bookPlatform.R
import com.pwr.bookPlatform.data.models.BookReview
import com.pwr.bookPlatform.ui.viewModels.BookshelfViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfView(
    modifier: Modifier = Modifier,
    bookshelfViewModel: BookshelfViewModel,
    onBack: () -> Unit,
    onNavigateToBookDetails: (String) -> Unit
) {
    BackHandler {
        onBack()
    }

    LaunchedEffect(Unit) {
        bookshelfViewModel.loadUserReviews()
    }

    val snackbarHostState = remember { SnackbarHostState() }

    bookshelfViewModel.snackbarMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            bookshelfViewModel.clearSnackbarMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.bookshelf_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.bookshelf_back)
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
            if (bookshelfViewModel.bookReviews.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.bookshelf_no_reviews),
                        fontSize = 18.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(bookshelfViewModel.bookReviews) { bookReview ->
                        ReviewItem(
                            bookReview = bookReview,
                            onDelete = { bookshelfViewModel.deleteReview(bookReview.review.id) },
                            onItemClick = {
                                onNavigateToBookDetails(bookReview.bookDetails?.key ?: "")
                            }
                        )

                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewItem(
    bookReview: BookReview,
    onDelete: () -> Unit,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onItemClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp, 120.dp)
                .padding(end = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            val coverId = bookReview.bookDetails?.covers?.firstOrNull()
            if (coverId != null) {
                AsyncImage(
                    model = "https://covers.openlibrary.org/b/id/$coverId-M.jpg",
                    contentDescription = stringResource(R.string.bookdetails_no_cover),
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.bookdetails_no_cover),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = bookReview.bookDetails?.title ?: stringResource(R.string.bookshelf_unknown_title),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            bookReview.bookDetails?.authors?.let { authors ->
                if (authors.isNotEmpty()) {
                    Text(
                        text = authors.joinToString(", ") { it.author ?: "" },
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.bookshelf_rating, bookReview.review.rating.toString()),
                fontSize = 14.sp
            )

            Text(
                text = stringResource(R.string.bookshelf_status, bookReview.review.status),
                fontSize = 14.sp
            )

            Text(
                text = stringResource(R.string.bookshelf_created_at, bookReview.review.createdAt),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        IconButton(
            onClick = onDelete,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.bookshelf_delete),
                tint = Color.Red
            )
        }
    }
}
