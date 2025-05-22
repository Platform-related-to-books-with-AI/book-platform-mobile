package com.pwr.BookPlatform.ui.Views

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pwr.BookPlatform.ui.ViewModels.BookDetailsViewModel

@Composable
fun BookDetailsView(
    modifier: Modifier = Modifier,
    bookDetailsViewModel: BookDetailsViewModel,
    bookId: String,
    onBack: () -> Unit
) {
    Text(
        text = "Book ID: $bookId",
        modifier = modifier
    )
}