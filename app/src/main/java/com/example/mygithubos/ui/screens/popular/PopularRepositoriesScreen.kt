package com.example.mygithubos.ui.screens.popular

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mygithubos.ui.components.RepositoryItem

@Composable
fun PopularRepositoriesScreen(
    onRepositoryClick: (String, String) -> Unit,
    viewModel: PopularRepositoriesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPopularRepositories()
    }

    when (uiState) {
        is PopularRepositoriesUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is PopularRepositoriesUiState.Success -> {
            val repositories = (uiState as PopularRepositoriesUiState.Success).repositories
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(repositories) { repository ->
                    RepositoryItem(
                        repository = repository,
                        onClick = {
                            onRepositoryClick(repository.owner.login, repository.name)
                        }
                    )
                }
            }
        }
        is PopularRepositoriesUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (uiState as PopularRepositoriesUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
} 