package com.example.mygithubos.ui.screens.repo_details

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mygithubos.ui.components.CreateIssueDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailsScreen(
    owner: String,
    repo: String,
    onBackClick: () -> Unit,
    viewModel: RepoDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val showCreateIssueDialog by viewModel.showCreateIssueDialog.collectAsState()

    LaunchedEffect(owner, repo) {
        viewModel.loadRepositoryDetails(owner, repo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$owner/$repo") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    Text(
                        text = uiState.error ?: "An error occurred",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.repository != null -> {
                    RepositoryDetails(
                        repository = uiState.repository!!,
                        onCreateIssue = { viewModel.showCreateIssueDialog() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    if (showCreateIssueDialog) {
        CreateIssueDialog(
            onDismiss = { viewModel.hideCreateIssueDialog() },
            onCreateIssue = { title, body ->
                viewModel.createIssue(title, body)
            }
        )
    }
}

@Composable
private fun RepositoryDetails(
    repository: com.example.mygithubos.data.model.Repository,
    onCreateIssue: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = repository.fullName,
            style = MaterialTheme.typography.headlineMedium
        )
        
        if (!repository.description.isNullOrBlank()) {
            Text(
                text = repository.description,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatItem("Stars", repository.stargazersCount)
            StatItem("Watchers", repository.watchersCount)
            StatItem("Forks", repository.forksCount)
        }

        if (repository.language != null) {
            Text(
                text = "Language: ${repository.language}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (repository.topics.isNotEmpty()) {
            Text(
                text = "Topics:",
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repository.topics.take(3).forEach { topic ->
                    FilterChip(
                        selected = false,
                        onClick = { },
                        label = { Text(topic) }
                    )
                }
            }
        }

        Button(
            onClick = onCreateIssue,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.BugReport,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Create Issue")
        }
    }
}

@Composable
private fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
} 