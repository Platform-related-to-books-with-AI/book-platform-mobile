package com.pwr.BookPlatform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.pwr.BookPlatform.Data.Services.AuthService
import com.pwr.BookPlatform.Data.api.RetrofitInstance
import com.pwr.BookPlatform.ui.ViewModels.LoginViewModel
import com.pwr.BookPlatform.ui.Views.LoginView
import com.pwr.BookPlatform.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    var AuthService: AuthService = AuthService(RetrofitInstance.authApi)
    var loginViewModel: LoginViewModel = LoginViewModel(AuthService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginView(modifier = Modifier.padding(innerPadding), loginViewModel = loginViewModel)
                }
            }
        }
    }
}