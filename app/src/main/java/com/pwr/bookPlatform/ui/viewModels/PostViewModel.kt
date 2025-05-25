package com.pwr.bookPlatform.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr.bookPlatform.data.models.Post
import com.pwr.bookPlatform.data.services.PostService
import com.pwr.bookPlatform.data.session.UserSession
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PostViewModel() : ViewModel() {

    var posts by mutableStateOf<List<Post>>(emptyList())
    var displayNickname by mutableStateOf("")
    var snackbarMessage by mutableStateOf<String?>(null)
    var currentPage by mutableStateOf(0)
    var totalPages by mutableStateOf(0)

    fun loadCurrentUserPosts() {
        UserSession.user?.nickname?.let { nickname ->
            displayNickname = "My"
            loadUserPosts(nickname)
        }
    }

    fun loadUserPosts(nickname: String, page: Int = 0) {
        viewModelScope.launch {
            snackbarMessage = null

            PostService.getUserPosts(nickname, page).fold(
                onSuccess = { response ->
                    posts = response.content
                    totalPages = response.totalPages
                    currentPage = response.number
                    displayNickname = if (nickname == UserSession.user?.nickname) "My" else nickname
                },
                onFailure = { exception ->
                    posts = emptyList()
                    displayNickname = ""
                    snackbarMessage = when (exception) {
                        is HttpException -> "Error loading posts: ${exception.code()}"
                        else -> exception.localizedMessage ?: "Unknown error"
                    }
                }
            )
        }
    }

    fun createPost(postText: String) {
        if (postText.isBlank()) {
            snackbarMessage = "Post text cannot be empty"
            return
        }

        val nickname = UserSession.user?.nickname ?: run {
            snackbarMessage = "User is not logged in"
            return
        }

        viewModelScope.launch {
            snackbarMessage = null

            PostService.createPost(postText, nickname).fold(
                onSuccess = { post ->
                    posts = listOf(post) + posts
                    snackbarMessage = "Post created successfully"
                },
                onFailure = { exception ->
                    snackbarMessage = when (exception) {
                        is HttpException -> "Error creating post: ${exception.code()}"
                        else -> exception.localizedMessage ?: "Unknown error"
                    }
                }
            )
        }
    }

    fun searchPostsByNickname(nickname: String) {
        if (nickname.isNotBlank()) {
            loadUserPosts(nickname)
        } else {
            snackbarMessage = "Please enter a valid nickname"
        }
    }

    fun clearSnackbarMessage() {
        snackbarMessage = null
    }
}
