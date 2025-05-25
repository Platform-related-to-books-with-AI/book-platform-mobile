package com.pwr.bookPlatform.ui.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pwr.bookPlatform.R
import com.pwr.bookPlatform.data.session.UserSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    onBack: () -> Boolean,
    onNavigate: () -> Unit,
    onNavigateToBookshelf: () -> Unit,
    onNavigateToPosts: () -> Unit,
    onLogout: () -> Unit
) {
    BackHandler {
        onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_title)) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onNavigate,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.nav_go_to_browse))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onNavigateToBookshelf,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.nav_my_bookshelf))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onNavigateToPosts,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.nav_my_posts))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        UserSession.clearAuthToken()
                        onLogout()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.nav_logout))
                }
            }
        }
    }
}
