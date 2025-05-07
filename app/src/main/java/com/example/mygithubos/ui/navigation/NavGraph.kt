package com.example.mygithubos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mygithubos.domain.repository.GitHubRepository
import com.example.mygithubos.navigation.NavRoutes
import com.example.mygithubos.ui.screens.home.HomeScreen
import com.example.mygithubos.ui.screens.login.LoginScreen
import com.example.mygithubos.ui.screens.repo_details.RepoDetailsScreen
import com.example.mygithubos.ui.screens.user_profile.UserProfileScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    repository: GitHubRepository
) {
    val isAuthenticated by repository.isAuthenticated().collectAsState(initial = false)

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) NavRoutes.HOME else NavRoutes.LOGIN
    ) {
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
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
        composable("${NavRoutes.REPO_DETAILS}/{owner}/{repo}") { backStackEntry ->
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