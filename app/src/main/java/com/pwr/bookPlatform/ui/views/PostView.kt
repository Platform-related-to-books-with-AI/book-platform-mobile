package com.pwr.bookPlatform.ui.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.pwr.bookPlatform.R
import com.pwr.bookPlatform.data.models.Post
import com.pwr.bookPlatform.data.session.UserSession
import com.pwr.bookPlatform.ui.viewModels.PostViewModel
import com.pwr.bookPlatform.data.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostView(
    modifier: Modifier = Modifier,
    postViewModel: PostViewModel,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }
    postViewModel.loadCurrentUserPosts()
    val snackbarHostState = remember { SnackbarHostState() }
    var showNewPostDialog by remember { mutableStateOf(false) }

    var searchNickname by remember { mutableStateOf(UserSession.user?.nickname ?: "") }
    var newPostText by remember { mutableStateOf("") }

    postViewModel.snackbarMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            postViewModel.clearSnackbarMessage()
        }
    }

    if (showNewPostDialog) {
        CreatePostDialog(
            newPostText = newPostText,
            onPostTextChange = { newPostText = it },
            onPost = {
                postViewModel.createPost(newPostText)
                newPostText = ""
                showNewPostDialog = false
            },
            onDismiss = { showNewPostDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (postViewModel.displayNickname.isNotEmpty()) {
                        if (postViewModel.displayNickname == "My") {
                            Text("My Posts")
                        } else {
                            Text("${postViewModel.displayNickname}'s Posts")
                        }
                    } else {
                        Text(stringResource(R.string.posts_title))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.posts_back)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchNickname,
                    onValueChange = { searchNickname = it },
                    label = { Text(stringResource(R.string.posts_search_user_id)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = { postViewModel.searchPostsByNickname(searchNickname) }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.posts_search)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showNewPostDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.posts_create_new))
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(postViewModel.posts) { post ->
                    PostItem(post = post)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostDialog(
    newPostText: String,
    onPostTextChange: (String) -> Unit,
    onPost: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.posts_create_new),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = newPostText,
                    onValueChange = onPostTextChange,
                    label = { Text(stringResource(R.string.posts_enter_text)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(onClick = onPost) {
                Text(stringResource(R.string.posts_post))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(android.R.string.cancel))
            }
        }
    )
}

@Composable
fun PostItem(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = post.text,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Divider(
                color = MaterialTheme.colorScheme.surfaceVariant,
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = DateUtils.formatDateTime(post.createdAt),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    fontStyle = FontStyle.Italic
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* Implementacja polubienia */ }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = stringResource(R.string.posts_likes, post.likes),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = post.likes.toString(),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

