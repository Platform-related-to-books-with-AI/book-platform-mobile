package com.pwr.bookPlatform

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
import com.pwr.bookPlatform.data.services.AuthService
import com.pwr.bookPlatform.data.services.BookService
import com.pwr.bookPlatform.data.api.RetrofitInstance
import com.pwr.bookPlatform.data.services.PostService
import com.pwr.bookPlatform.ui.viewModels.BookDetailsViewModel
import com.pwr.bookPlatform.ui.viewModels.BrowserViewModel
import com.pwr.bookPlatform.ui.viewModels.LoginViewModel
import com.pwr.bookPlatform.ui.viewModels.BookshelfViewModel
import com.pwr.bookPlatform.ui.viewModels.PostViewModel
import com.pwr.bookPlatform.ui.viewModels.AccountViewModel
import com.pwr.bookPlatform.ui.views.BookDetailsView
import com.pwr.bookPlatform.ui.views.BrowserView
import com.pwr.bookPlatform.ui.views.LoginView
import com.pwr.bookPlatform.ui.theme.MyApplicationTheme
import com.pwr.bookPlatform.ui.views.BookshelfView
import com.pwr.bookPlatform.ui.views.HomeView
import com.pwr.bookPlatform.ui.views.PostView
import com.pwr.bookPlatform.ui.views.AccountView

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Browse : Screen("browse")
    object BookDetails : Screen("book_details/{key}") {
        fun createRoute(key: String) = "book_details/$key"
    }
    object Home : Screen("home")
    object Bookshelf : Screen("bookshelf")
    object Posts : Screen("posts")
    object Account : Screen("account")
}

class MainActivity : ComponentActivity() {
    private val authService = AuthService(RetrofitInstance.authApi)
    private val bookService = BookService(RetrofitInstance.bookApi, RetrofitInstance.reviewApi)
    private val postService = PostService(RetrofitInstance.postApi)

    private val loginViewModel = LoginViewModel(authService)
    private val bookViewModel = BrowserViewModel(bookService)
    private val bookDetailsViewModel = BookDetailsViewModel(bookService)
    private val bookshelfViewModel = BookshelfViewModel(bookService)
    private val postViewModel = PostViewModel(postService)
    private val accountViewModel = AccountViewModel(authService)

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
                        bookshelfViewModel = bookshelfViewModel,
                        postViewModel = postViewModel,
                        accountViewModel = accountViewModel,
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
    bookshelfViewModel: BookshelfViewModel,
    postViewModel: PostViewModel,
    accountViewModel: AccountViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
    ) {
        composable(Screen.Login.route) {
            LoginView(
                loginViewModel = loginViewModel,
                onNavigate = {
                    navController.navigate(Screen.Home.route)
                }
            )
        }
        composable(Screen.Home.route) {
            HomeView (
                modifier = modifier,
                onBack = {
                    navController.popBackStack()
                    true
                },
                onNavigate = {
                    navController.navigate(Screen.Browse.route)
                },
                onNavigateToBookshelf = {
                    navController.navigate(Screen.Bookshelf.route)
                },
                onNavigateToPosts = {
                    navController.navigate(Screen.Posts.route)
                },
                onNavigateToAccount = {
                    navController.navigate(Screen.Account.route)
                },
                onLogout = {
                    loginViewModel.authenticated = false
                    loginViewModel.user = null
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
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
                    navController.popBackStack()
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
        composable(Screen.Bookshelf.route) {
            BookshelfView(
                modifier = modifier,
                bookshelfViewModel = bookshelfViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToBookDetails = { bookId ->
                    val encodedKey = Uri.encode(bookId)
                    navController.navigate(Screen.BookDetails.createRoute(encodedKey))
                }
            )
        }
        composable(Screen.Posts.route) {
            PostView(
                modifier = modifier,
                postViewModel = postViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Account.route) {
            AccountView(
                modifier = modifier,
                accountViewModel = accountViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
