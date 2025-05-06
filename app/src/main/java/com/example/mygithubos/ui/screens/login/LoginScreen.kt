package com.example.mygithubos.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Welcome to GitHub Client",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Text(
                text = "Sign in to access your GitHub account",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = { viewModel.login() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Sign in with GitHub")
            }

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "An error occurred",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
} 