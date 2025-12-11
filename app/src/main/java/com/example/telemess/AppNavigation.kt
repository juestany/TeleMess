package com.example.telemess

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// Sealed class with all routes used in the app
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {

    val mainViewModel: MainViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {

        // Login, register navigation
        composable(Screen.Login.route) {
            LoginScreen(
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                viewModel = mainViewModel
            )
        }

        // Register, back to login
        composable(Screen.Register.route) {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = mainViewModel
            )
        }

        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}
