package com.pwr.bookPlatform.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr.bookPlatform.data.models.UpdateUserRequest
import com.pwr.bookPlatform.data.models.UserResponse
import com.pwr.bookPlatform.data.services.AuthService
import com.pwr.bookPlatform.data.session.UserSession
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AccountViewModel(
    private val authService: AuthService
) : ViewModel() {

    var user by mutableStateOf<UserResponse?>(null)
    var snackbarMessage by mutableStateOf<String?>(null)

    init {
        loadUserData()
    }

    fun loadUserData() {
        UserSession.user?.let { user ->
            this.user = user
        }
    }

    fun saveProfileChanges(email: String, nickname: String, password: String = "") {
        val currentUser = UserSession.user ?: run {
            snackbarMessage = "User is not logged in"
            return
        }

        val updatedEmail = if (email.isBlank()) currentUser.email else email
        val updatedNickname = if (nickname.isBlank()) currentUser.nickname else nickname

        if (updatedEmail.isBlank()) {
            snackbarMessage = "Email cannot be empty"
            return
        }

        if (updatedNickname.isBlank() || updatedNickname.length < 3 || updatedNickname.length > 20) {
            snackbarMessage = "Nickname must be 3-20 characters long"
            return
        }

        snackbarMessage = null

        val updateRequest = if (password.isBlank()) {
            UpdateUserRequest(updatedEmail, updatedNickname, null)
        } else {
            UpdateUserRequest(updatedEmail, updatedNickname, password)
        }

        viewModelScope.launch {
            val token = UserSession.token ?: run {
                snackbarMessage = "Missing authentication token"
                return@launch
            }

            authService.updateUser(currentUser.id, token, updateRequest).fold(
                onSuccess = { updatedUser ->
                    user = updatedUser
                    snackbarMessage = "Profile updated successfully. Please log in again."

                    UserSession.clearSession()
                },
                onFailure = { exception ->
                    snackbarMessage = when (exception) {
                        is HttpException -> when (exception.code()) {
                            400 -> "Invalid data"
                            401 -> "Unauthorized access"
                            403 -> "Permission denied"
                            else -> "Profile update error: ${exception.code()}"
                        }
                        else -> exception.localizedMessage ?: "Unknown error"
                    }
                }
            )
        }
    }

    fun clearSnackbarMessage() {
        snackbarMessage = null
    }
}
