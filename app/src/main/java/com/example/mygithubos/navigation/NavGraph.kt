package com.example.mygithubos.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mygithubos.domain.repository.GitHubRepository
import com.example.mygithubos.ui.screens.home.HomeScreen
import com.example.mygithubos.ui.screens.login.LoginScreen
import com.example.mygithubos.ui.screens.repo_details.RepoDetailsScreen
import com.example.mygithubos.ui.screens.user_profile.UserProfileScreen

private const val TAG = "NavGraph"

@Composable
fun NavGraph(
    navController: NavHostController,
    repository: GitHubRepository
) {
    val isAuthenticated by repository.isAuthenticated().collectAsState(initial = false)
    
    Log.d(TAG, "Current authentication state: $isAuthenticated")

    LaunchedEffect(isAuthenticated) {
        Log.d(TAG, "Authentication state changed: $isAuthenticated")
        if (!isAuthenticated) {
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(NavRoutes.HOME) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) NavRoutes.HOME else NavRoutes.LOGIN
    ) {
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    Log.d(TAG, "Login successful, navigating to home")
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.HOME) {
            HomeScreen(
                onRepoClick = { owner, repo ->
                    navController.navigate("${NavRoutes.REPO_DETAILS}/$owner/$repo")
                },
                onProfileClick = {
                    navController.navigate(NavRoutes.USER_PROFILE)
                }
            )
        }

        composable(
            route = "${NavRoutes.REPO_DETAILS}/{owner}/{repo}",
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
    }
} 