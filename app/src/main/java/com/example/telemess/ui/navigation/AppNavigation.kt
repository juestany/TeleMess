package com.example.telemess.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.telemess.data.db.AppDatabase
import com.example.telemess.data.repository.SettingsRepository
import com.example.telemess.domain.model.MissedCallProcessor
import com.example.telemess.sms.FakeSmsSender
import com.example.telemess.ui.home.HomeScreen
import com.example.telemess.ui.home.HomeScreenViewModel
import com.example.telemess.ui.home.HomeScreenViewModelFactory
import com.example.telemess.ui.permissions.PermissionsScreen
import com.example.telemess.ui.quiet.QuietHoursScreen

// Sealed class with all routes used in the app
sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object QuietHours : Screen("quiet_hours", Icons.Filled.Settings, "Quiet Hours")
    object Home : Screen("home", Icons.Filled.Home, "Home")
    object Permissions : Screen("permissions", Icons.Filled.Security, "Permissions")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // --- Repositories and Processor ---
    val db = remember { AppDatabase.getInstance(context) }
    val settingsRepository = remember { SettingsRepository(db.quietHoursDao()) }
    val callRepository = remember { com.example.telemess.data.repository.CallRepository(db.missedCallDao()) }
    val smsSender = remember { FakeSmsSender() } // Use real AndroidSmsSender() on device
    val processor = remember { MissedCallProcessor(db, settingsRepository, smsSender) }

    // --- Home ViewModel ---
    val homeViewModel: HomeScreenViewModel = viewModel(
        factory = HomeScreenViewModelFactory(settingsRepository, callRepository)
    )

    LaunchedEffect(Unit) {
        settingsRepository.getOrCreateDefault() // preload DB
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Quiet Hours") },
                actions = {
                    Switch(
                        checked = homeViewModel.quietHoursEnabled.collectAsState().value,
                        onCheckedChange = homeViewModel::toggleQuietHours
                    )
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(homeViewModel, processor) }
            composable(Screen.QuietHours.route) { QuietHoursScreen(repository = settingsRepository) }
            composable(Screen.Permissions.route) { PermissionsScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val currentRoute =
            navController.currentBackStackEntryAsState().value?.destination?.route

        listOf(
            Screen.QuietHours,
            Screen.Home,
            Screen.Permissions
        ).forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                },
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) }
            )
        }
    }
}


