package com.example.mygithubos.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubos.data.auth.GitHubOAuthConfig
import com.example.mygithubos.data.auth.GitHubOAuthService
import com.example.mygithubos.domain.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val authUrl: String = "${GitHubOAuthConfig.AUTH_URL}?" +
            "client_id=${GitHubOAuthConfig.CLIENT_ID}&" +
            "redirect_uri=${GitHubOAuthConfig.REDIRECT_URI}&" +
            "scope=${GitHubOAuthConfig.SCOPE}"
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: GitHubRepository,
    private val oAuthService: GitHubOAuthService
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun handleOAuthCallback(code: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = oAuthService.getAccessToken(
                    clientId = GitHubOAuthConfig.CLIENT_ID,
                    clientSecret = GitHubOAuthConfig.CLIENT_SECRET,
                    code = code,
                    redirectUri = GitHubOAuthConfig.REDIRECT_URI
                )
                repository.login(response.access_token)
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Login failed"
                )
            }
        }
    }
} 