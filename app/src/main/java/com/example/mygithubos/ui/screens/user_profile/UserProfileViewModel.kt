package com.example.mygithubos.ui.screens.user_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.data.model.User
import com.example.mygithubos.domain.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val repository: GitHubRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserProfileUiState>(UserProfileUiState.Loading)
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                val user = repository.getCurrentUser()
                val repos = repository.getUserRepositories(1, 30)
                _uiState.value = UserProfileUiState.Success(user, repos)
            } catch (e: Exception) {
                _uiState.value = UserProfileUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                repository.logout()
                _uiState.value = UserProfileUiState.LoggedOut
            } catch (e: Exception) {
                _uiState.value = UserProfileUiState.Error(e.message ?: "Logout failed")
            }
        }
    }
}

sealed class UserProfileUiState {
    object Loading : UserProfileUiState()
    data class Success(val user: User, val repositories: List<Repository>) : UserProfileUiState()
    data class Error(val message: String) : UserProfileUiState()
    object LoggedOut : UserProfileUiState()
} 