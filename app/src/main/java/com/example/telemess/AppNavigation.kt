package com.example.telemess

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// Sealed class with all routes used in the app
sealed class Screen(val route: String) {
    object Home : Screen("home")
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {

    val mainViewModel: MainViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}
