package com.example.mygithubos.ui.screens.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.domain.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularRepositoriesViewModel @Inject constructor(
    private val repository: GitHubRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PopularRepositoriesUiState>(PopularRepositoriesUiState.Loading)
    val uiState: StateFlow<PopularRepositoriesUiState> = _uiState.asStateFlow()

    fun loadPopularRepositories() {
        viewModelScope.launch {
            try {
                // 搜索星标数超过 1000 的仓库，按星标数降序排序
                val response = repository.searchRepositories(
                    query = "stars:>1000",
                    sort = "stars",
                    order = "desc",
                    perPage = 20
                )
                _uiState.value = PopularRepositoriesUiState.Success(response.items)
            } catch (e: Exception) {
                _uiState.value = PopularRepositoriesUiState.Error(e.message ?: "Failed to load repositories")
            }
        }
    }
}

sealed class PopularRepositoriesUiState {
    object Loading : PopularRepositoriesUiState()
    data class Success(val repositories: List<Repository>) : PopularRepositoriesUiState()
    data class Error(val message: String) : PopularRepositoriesUiState()
} 