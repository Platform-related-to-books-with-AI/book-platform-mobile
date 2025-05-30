package com.pwr.bookPlatform.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr.bookPlatform.data.models.Post
import com.pwr.bookPlatform.data.services.PostService
import com.pwr.bookPlatform.data.services.LikeService
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

    fun toggleLike(post: Post) {
        viewModelScope.launch {
            snackbarMessage = null

            LikeService.toggleLike(post.id).fold(
                onSuccess = { response ->
                    // Aktualizacja stanu posta w liście
                    posts = posts.map {
                        if (it.id == post.id) {
                            it.copy(likes = response.likes, likedByUser = response.likedByUser)
                        } else {
                            it
                        }
                    }
                },
                onFailure = { exception ->
                    snackbarMessage = when (exception) {
                        is HttpException -> {
                            when (exception.code()) {
                                401 -> "Musisz być zalogowany, aby polubić post"
                                403 -> "Brak uprawnień do wykonania tej akcji"
                                404 -> "Post nie został znaleziony"
                                409 -> "Konflikt - spróbuj ponownie później"
                                else -> "Wystąpił błąd podczas przetwarzania żądania"
                            }
                        }
                        else -> "Nie udało się połączyć z serwerem"
                    }
                }
            )
        }
    }
}
