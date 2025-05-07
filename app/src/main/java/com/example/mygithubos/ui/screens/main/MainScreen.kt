package com.example.mygithubos.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mygithubos.ui.screens.popular.PopularRepositoriesScreen
import com.example.mygithubos.ui.screens.search.SearchScreen
import com.example.mygithubos.ui.screens.user_profile.UserProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val isAuthenticated by viewModel.isAuthenticated.collectAsState(initial = false)
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = "Popular") },
                    label = { Text("Popular") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    label = { Text("Search") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> PopularRepositoriesScreen(
                    onRepositoryClick = { owner, repo ->
                        navController.navigate("repository_detail/$owner/$repo")
                    }
                )
                1 -> SearchScreen(
                    onRepositoryClick = { owner, repo ->
                        navController.navigate("repository_detail/$owner/$repo")
                    },
                    onProfileClick = {
                        selectedTab = 2
                    }
                )
                2 -> {
                    if (isAuthenticated) {
                        UserProfileScreen(navController = navController)
                    } else {
                        // 显示登录按钮
                        Box(modifier = Modifier.fillMaxSize()) {
                            Button(
                                onClick = {
                                    navController.navigate("login")
                                }
                            ) {
                                Text("Login")
                            }
                        }
                    }
                }
            }
        }
    }
} 