package com.pwr.BookPlatform.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr.BookPlatform.data.models.AuthResponse
import com.pwr.BookPlatform.data.models.LoginRequest
import com.pwr.BookPlatform.data.models.RegisterRequest
import com.pwr.BookPlatform.data.services.AuthService
import com.pwr.BookPlatform.data.session.UserSession
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val authService: AuthService) : ViewModel() {

    var authenticated by mutableStateOf(false)
    var user by mutableStateOf<AuthResponse?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        UserSession.getAuthToken()
        load()
    }

    fun load(){
        var token = UserSession.token
        if (token != null) {
            viewModelScope.launch {
                authService.getCurrentUser(token)
                    .onSuccess { auth ->
                        user = auth
                        authenticated = true
                        UserSession.saveAuthToken(auth.token, auth.user)
                    }
                    .onFailure { error ->
                        UserSession.clearAuthToken()
                        authenticated = false
                        user = null
                    }
            }
        } else {
            authenticated = false
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            errorMessage = null
            val result = authService.sendLoginRequest(
                loginRequest = LoginRequest(email, password)
            )

            result
                .onSuccess { auth ->
                    user = auth
                    authenticated = true
                    UserSession.saveAuthToken(auth.token, auth.user)
                }
                .onFailure { error ->
                    if(error is HttpException && error.code() == 403) {
                        errorMessage = "Email not activated"
                    }
                    else {
                        errorMessage = error.message ?: "Login failed"
                    }
                }
        }
    }

    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {
            errorMessage = null
            val result = authService.sendRegisterRequest(
                registerRequest = RegisterRequest(email, username, password)
            )
            result
                .onSuccess { auth ->
                    user = auth
                    login(email, password)
                }
                .onFailure { error ->
                    errorMessage = error.message ?: "Registration failed"
                }
        }
    }
}
