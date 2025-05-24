package com.pwr.bookPlatform.ui.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pwr.bookPlatform.data.session.UserSession

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    onBack: () -> Boolean,
    onNavigate: () -> Unit,
    onNavigateToBookshelf: () -> Unit,
    onLogout: () -> Unit
) {
    BackHandler {
        onBack()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onNavigate,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Go to Browse")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToBookshelf,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "My Bookshelf")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                UserSession.clearAuthToken()
                onLogout()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Logout")
        }
    }
}
