package com.pwr.bookPlatform.ui.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pwr.bookPlatform.data.models.BookDoc
import com.pwr.bookPlatform.data.models.SortType
import com.pwr.bookPlatform.data.models.getLabel
import com.pwr.bookPlatform.ui.viewModels.BrowserViewModel
import com.gowtham.ratingbar.RatingBar
import com.pwr.bookPlatform.R
import java.util.Locale

@Composable
fun BrowserView(
    modifier: Modifier = Modifier,
    browserViewModel: BrowserViewModel,
    onBack: () -> Unit,
    onNavigate: (BookDoc) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedSort by remember { mutableStateOf(SortType.RATING_DESC) }

    LaunchedEffect(Unit) {
        if (browserViewModel.books.isEmpty()) {
            browserViewModel.search("lord", sort = selectedSort.sortValue)
        }
    }

    BackHandler {
        onBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (!browserViewModel.errorMessage.isNullOrEmpty()) {
            Text(
                text = browserViewModel.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text(stringResource(id = R.string.browser_search_books)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { browserViewModel.search(query, sort = selectedSort.sortValue) }) {
                Text(stringResource(id = R.string.browser_search))
            }
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.Menu, contentDescription = "Sortuj")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    SortType.values().forEach { sortType ->
                        DropdownMenuItem(
                            text = { Text(sortType.getLabel()) },
                            onClick = {
                                selectedSort = sortType
                                expanded = false
                                browserViewModel.search(query, sort = sortType.sortValue)
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(browserViewModel.books) { book ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(book) }
                ) {
                    BookListItem(book = book)
                }
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun BookListItem(book: BookDoc) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        if (book.cover_i != null) {
            AsyncImage(
                model = "https://covers.openlibrary.org/b/id/${book.cover_i}-S.jpg",
                contentDescription = null,
                modifier = Modifier.size(56.dp).padding(end = 8.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = book.author_name?.joinToString(", ") ?: stringResource(id = R.string.browser_unknown_author),
                style = MaterialTheme.typography.bodyMedium
            )
            book.first_publish_year?.let { year ->
                Text(
                    text = stringResource(id = R.string.browser_first_published, year),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            book.ratings_average?.let { rating ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    RatingBar(
                        value = rating,
                        onValueChange = {},
                        onRatingChanged = {},
                        modifier = Modifier.height(20.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format(Locale.getDefault(), "%.1f", rating),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
