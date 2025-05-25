package com.pwr.bookPlatform.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr.bookPlatform.data.models.AuthResponse
import com.pwr.bookPlatform.data.models.LoginRequest
import com.pwr.bookPlatform.data.models.RegisterRequest
import com.pwr.bookPlatform.data.services.AuthService
import com.pwr.bookPlatform.data.session.UserSession
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel : ViewModel() {

    var authenticated by mutableStateOf(false)
    var user by mutableStateOf<AuthResponse?>(null)
    var snackbarMessage by mutableStateOf<String?>(null)

    init {
        UserSession.getAuthToken()
        load()
    }

    fun load(){
        var token = UserSession.token
        if (token != null) {
            viewModelScope.launch {
                AuthService.getCurrentUser(token).fold(
                    onSuccess = { auth ->
                        user = auth
                        authenticated = true
                        UserSession.saveAuthToken(auth.token, auth.user)
                    },
                    onFailure = { error ->
                        UserSession.clearAuthToken()
                        authenticated = false
                        user = null
                    }
                )
            }
        } else {
            authenticated = false
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            snackbarMessage = null
            AuthService.sendLoginRequest(
                loginRequest = LoginRequest(email, password)
            ).fold(
                onSuccess = { auth ->
                    user = auth
                    authenticated = true
                    UserSession.saveAuthToken(auth.token, auth.user)
                },
                onFailure = { error ->
                    if(error is HttpException && error.code() == 403) {
                        snackbarMessage = "Email not activated"
                    }
                    else {
                        snackbarMessage = error.message ?: "Login failed"
                    }
                }
            )
        }
    }

    fun register(email: String, nickname: String, password: String) {
        viewModelScope.launch {
            snackbarMessage = null
            AuthService.sendRegisterRequest(
                registerRequest = RegisterRequest(email, nickname, password)
            ).fold(
                onSuccess = { auth ->
                    user = auth
                    login(email, password)
                },
                onFailure = { error ->
                    snackbarMessage = error.message ?: "Registration failed"
                }
            )
        }
    }

    fun clearSnackbarMessage() {
        snackbarMessage = null
    }
}
