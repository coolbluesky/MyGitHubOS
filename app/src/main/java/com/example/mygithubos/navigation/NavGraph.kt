package com.example.mygithubos.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mygithubos.domain.repository.GitHubRepository
import com.example.mygithubos.ui.screens.login.LoginScreen
import com.example.mygithubos.ui.screens.main.MainScreen
import com.example.mygithubos.ui.screens.repo_details.RepoDetailsScreen
import com.example.mygithubos.ui.screens.search.SearchScreen
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
            navController.navigate("login") {
                popUpTo("search") { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "main" else "login"
    ) {
        composable("main") {
            MainScreen(navController = navController)
        }

        composable("search") {
            SearchScreen(
                onRepositoryClick = { owner, repo ->
                    navController.navigate("repository_detail/$owner/$repo")
                },
                onProfileClick = {
                    navController.navigate("user_profile")
                }
            )
        }

        composable("repository_detail/{owner}/{repo}") { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: return@composable
            val repo = backStackEntry.arguments?.getString("repo") ?: return@composable
            RepoDetailsScreen(
                owner = owner,
                repo = repo,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("user_profile") {
            UserProfileScreen(
                navController = navController
            )
        }
    }
} 