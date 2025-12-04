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
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {

    val mainViewModel: MainViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {

        // Login → Register navigation
        composable(Screen.Login.route) {
            LoginScreen(
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                viewModel = mainViewModel
            )
        }

        // Register → Back to Login
        composable(Screen.Register.route) {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = mainViewModel
            )
        }
    }
}