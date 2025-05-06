package com.example.mygithubos.ui.screens.repo_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubos.data.model.Repository
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(RepoDetailsUiState())
    val uiState: StateFlow<RepoDetailsUiState> = _uiState.asStateFlow()

    init {
        val owner = savedStateHandle.get<String>("owner")
        val repo = savedStateHandle.get<String>("repo")
        if (owner != null && repo != null) {
            loadRepositoryDetails(owner, repo)
        }
    }

    private fun loadRepositoryDetails(owner: String, repo: String) {
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
} 