package com.pwr.BookPlatform

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pwr.BookPlatform.data.services.AuthService
import com.pwr.BookPlatform.data.services.BookService
import com.pwr.BookPlatform.data.api.RetrofitInstance
import com.pwr.BookPlatform.ui.viewModels.BookDetailsViewModel
import com.pwr.BookPlatform.ui.viewModels.BrowserViewModel
import com.pwr.BookPlatform.ui.viewModels.LoginViewModel
import com.pwr.BookPlatform.ui.views.BookDetailsView
import com.pwr.BookPlatform.ui.views.BrowserView
import com.pwr.BookPlatform.ui.views.LoginView
import com.pwr.BookPlatform.ui.views.PendingActivationView
import com.pwr.BookPlatform.ui.theme.MyApplicationTheme

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object PendingActivation : Screen("pending_activation")
    object Browse : Screen("browse")
    object BookDetails : Screen("book_details/{key}") {
        fun createRoute(key: String) = "book_details/$key"
    }
}


class MainActivity : ComponentActivity() {
    private val authService = AuthService(RetrofitInstance.authApi)
    private val bookService = BookService(RetrofitInstance.bookApi)
    private val loginViewModel = LoginViewModel(authService)
    private val bookViewModel = BrowserViewModel(bookService)
    private val bookDetailsViewModel = BookDetailsViewModel(bookService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        loginViewModel = loginViewModel,
                        bookViewModel = bookViewModel,
                        bookDetailsViewModel = bookDetailsViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    loginViewModel: LoginViewModel,
    bookViewModel: BrowserViewModel,
    bookDetailsViewModel: BookDetailsViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Browse.route,
    ) {
        composable(Screen.Login.route) {
            LoginView(
                loginViewModel = loginViewModel,
                onNavigate = {
                    if (loginViewModel.authenticated) {
                        navController.navigate(Screen.Browse.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                    else if (loginViewModel.isPendingActivation) {
                        navController.navigate(Screen.PendingActivation.route)
                    }
                }
            )
        }
        composable(Screen.PendingActivation.route) {
            PendingActivationView(
                loginViewModel = loginViewModel,
                onBack = {
                    loginViewModel.isPendingActivation = false
                    loginViewModel.user = null
                    loginViewModel.errorMessage = null
                    navController.popBackStack()
                    true
                },
                onNavigate = {
                    if (loginViewModel.authenticated) {
                        navController.navigate(Screen.Browse.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable(Screen.Browse.route) {
            BrowserView(
                modifier = modifier,
                browserViewModel = bookViewModel,
                onBack = {
                    navController.popBackStack(Screen.Login.route, inclusive = false)
                },
                onNavigate = { book ->
                    val encodedKey = Uri.encode(book.key)
                    navController.navigate(Screen.BookDetails.createRoute(encodedKey))
                }
            )
        }
        composable(Screen.BookDetails.route) { backStackEntry ->
            val bookKey = backStackEntry.arguments?.getString("key")?.let { Uri.decode(it) }

            if (bookKey != null) {
                BookDetailsView(
                    modifier = modifier,
                    bookId = bookKey,
                    bookDetailsViewModel = bookDetailsViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
