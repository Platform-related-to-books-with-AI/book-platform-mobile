package com.pwr.BookPlatform.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    onBack: () -> Boolean,
    onNavigate: () -> Unit,
) {
    Button(
        onClick = onNavigate,
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(text = "Go to Browse")
    }
}
