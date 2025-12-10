package nalgoticas.salle.cinetrack

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import nalgoticas.salle.cinetrack.data.Preferences
import nalgoticas.salle.cinetrack.ui.auth.LoginScreen
import nalgoticas.salle.cinetrack.ui.auth.RegisterScreen
import nalgoticas.salle.cinetrack.ui.components.CineTrackBottomBar
import nalgoticas.salle.cinetrack.ui.discover.DiscoverScreen
import nalgoticas.salle.cinetrack.ui.screens.home.HomeScreen
import nalgoticas.salle.cinetrack.ui.screens.home.HomeViewModel
import nalgoticas.salle.cinetrack.ui.screens.home.MovieDetailScreen
import nalgoticas.salle.cinetrack.ui.screens.home.ProfileScreen
import nalgoticas.salle.cinetrack.ui.screens.home.diaryScreen.DiaryScreen
import nalgoticas.salle.cinetrack.ui.theme.CineTrackTheme
import nalgoticas.salle.cinetrack.ui.theme.background

@Composable
fun CineTrackApp() {
    val navController = rememberNavController()
    val bg = background
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val homeViewModel: HomeViewModel = viewModel()
    Scaffold(
        containerColor = bg,
        bottomBar = {
            if (currentRoute != "login" && currentRoute != "signup") {
                CineTrackBottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            // LOGIN
            composable("login") {
                LoginScreen(
                    nav = navController
                )
            }

            // SIGN UP
            composable("signup") {
                RegisterScreen(
                    navController
                )
            }

            // HOME
            composable("home") {
                HomeScreen(
                    onMovieClick = { movie ->
                        navController.navigate("details/${movie.id}")
                    },
                    viewModel = homeViewModel
                )
            }

            // DISCOVER
            composable("discover") {
                DiscoverScreen(
                    onMovieClick = { movie ->
                        navController.navigate("details/${movie.id}")
                    }, homeViewModel = homeViewModel
                )
            }

            // DIARY
            composable("diary") {
                DiaryScreen(
                    homeViewModel = homeViewModel
                )
            }

            // PROFILE
            composable("profile") {
                ProfileScreen(
                    onLogout = {
                        Preferences.clearSettinga()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }, homeViewModel = homeViewModel
                )
            }


            // DETAILS
            composable(
                route = "details/{movieId}",
                arguments = listOf(
                    navArgument("movieId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("movieId") ?: return@composable
                MovieDetailScreen(
                    movieId = id,
                    onBack = { navController.popBackStack() },
                    homeViewModel = homeViewModel,
                )
            }
        }
    }
}
