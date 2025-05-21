package com.pwr.BookPlatform.ui.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwr.BookPlatform.Data.Models.AuthResponse
import com.pwr.BookPlatform.Data.Models.LoginRequest
import com.pwr.BookPlatform.Data.Models.RegisterRequest
import com.pwr.BookPlatform.Data.Services.AuthService
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val authService: AuthService) : ViewModel() {

    var isPendingActivation by mutableStateOf(false)
    var user by mutableStateOf<AuthResponse?>(null)
    var errorMessage by mutableStateOf<String?>(null)


    fun login(email: String, password: String) {
        viewModelScope.launch {
            errorMessage = null
            val result = authService.sendLoginRequest(
                loginRequest = LoginRequest(email, password)
            )

            result
                .onSuccess { auth ->
                    user = auth
                    isPendingActivation = !auth.user.active
                }
                .onFailure { error ->
                    if(error is HttpException && error.code() == 403) {
                        errorMessage = "Email not activated"
                        isPendingActivation = true
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
                    isPendingActivation = !auth.user.active
                }
                .onFailure { error ->
                    errorMessage = error.message ?: "Registration failed"
                }
        }
    }

    fun checkUserStatus() {
        viewModelScope.launch {
            if(user == null) {
                return@launch
            }
            val result = authService.getCurrentUser(user!!.token)

            result
                .onSuccess { auth ->
                    user = auth
                    isPendingActivation = auth.user.active
                }
                .onFailure { error ->
                    errorMessage = error.message ?: "Email not activated"
                }
        }
    }
}
