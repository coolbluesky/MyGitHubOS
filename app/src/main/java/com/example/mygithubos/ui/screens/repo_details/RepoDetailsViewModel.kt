package com.example.mygithubos.ui.screens.repo_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubos.data.model.IssueRequest
import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.domain.repository.GitHubRepository
import com.example.mygithubos.domain.usecase.GetRepositoryDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RepoDetailsUiState(
    val repository: Repository? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RepoDetailsViewModel @Inject constructor(
    private val getRepositoryDetailsUseCase: GetRepositoryDetailsUseCase,
    private val repository: GitHubRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(RepoDetailsUiState())
    val uiState: StateFlow<RepoDetailsUiState> = _uiState.asStateFlow()

    private val _showCreateIssueDialog = MutableStateFlow(false)
    val showCreateIssueDialog: StateFlow<Boolean> = _showCreateIssueDialog.asStateFlow()

    init {
        val owner = savedStateHandle.get<String>("owner")
        val repo = savedStateHandle.get<String>("repo")
        if (owner != null && repo != null) {
            loadRepositoryDetails(owner, repo)
        }
    }

    fun loadRepositoryDetails(owner: String, repo: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val repository = getRepositoryDetailsUseCase(owner, repo)
                _uiState.update { 
                    it.copy(
                        repository = repository,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        error = e.message ?: "An error occurred",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun showCreateIssueDialog() {
        _showCreateIssueDialog.value = true
    }

    fun hideCreateIssueDialog() {
        _showCreateIssueDialog.value = false
    }

    fun createIssue(title: String, body: String) {
        val repo = _uiState.value.repository ?: return
        viewModelScope.launch {
            try {
                repository.createIssue(repo.owner.login, repo.name, IssueRequest(title, body))
                _showCreateIssueDialog.value = false
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        error = e.message ?: "Failed to create issue"
                    )
                }
            }
        }
    }
} 