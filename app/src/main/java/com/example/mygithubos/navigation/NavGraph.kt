package com.example.mygithubos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mygithubos.ui.screens.home.HomeScreen
import com.example.mygithubos.ui.screens.login.LoginScreen
import com.example.mygithubos.ui.screens.repo_details.RepoDetailsScreen
import com.example.mygithubos.ui.screens.user_profile.UserProfileScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                onRepoClick = { owner, repo ->
                    navController.navigate("repo_details/$owner/$repo")
                },
                onProfileClick = {
                    navController.navigate(NavRoutes.USER_PROFILE)
                }
            )
        }

        composable(
            route = NavRoutes.REPO_DETAILS,
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: return@composable
            val repo = backStackEntry.arguments?.getString("repo") ?: return@composable
            RepoDetailsScreen(
                owner = owner,
                repo = repo,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.USER_PROFILE) {
            UserProfileScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
    }
} 