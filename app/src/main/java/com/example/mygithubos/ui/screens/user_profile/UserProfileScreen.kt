package com.example.mygithubos.ui.screens.user_profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.data.model.User
import com.example.mygithubos.ui.components.RepositoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.logout() }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is UserProfileUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UserProfileUiState.Success -> {
                val user = (uiState as UserProfileUiState.Success).user
                val repositories = (uiState as UserProfileUiState.Success).repositories
                UserProfileContent(
                    user = user,
                    repositories = repositories,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is UserProfileUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as UserProfileUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            is UserProfileUiState.LoggedOut -> {
                LaunchedEffect(Unit) {
                    navController.navigate("login") {
                        popUpTo("user_profile") { inclusive = true }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserProfileContent(
    user: User,
    repositories: List<Repository>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            UserInfo(user = user)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Repositories",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(repositories) { repository ->
            RepositoryItem(repository = repository)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun UserInfo(user: User) {
    Column {
        Text(
            text = user.login,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = user.bio ?: "No bio",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = user.followers.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Followers",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = user.following.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Following",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = user.publicRepos.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Repos",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
} 