package com.example.mygithubos.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.data.model.SearchResponse
import com.example.mygithubos.domain.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortBy {
    STARS,
    UPDATED
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: GitHubRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Success(emptyList()))
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var currentQuery: String = ""
    private var _selectedLanguage: String? = null
    val selectedLanguage: String? get() = _selectedLanguage
    private var _sortBy: SortBy = SortBy.STARS
    val sortBy: SortBy get() = _sortBy

    val availableLanguages = listOf(
        "Java",
        "Kotlin",
        "Python",
        "JavaScript",
        "TypeScript",
        "Go",
        "Rust",
        "C++",
        "C#",
        "Swift"
    )

    fun searchRepositories(query: String) {
        currentQuery = query
        performSearch()
    }

    fun setLanguage(language: String?) {
        _selectedLanguage = language
        performSearch()
    }

    fun setSortBy(sortBy: SortBy) {
        _sortBy = sortBy
        performSearch()
    }

    private fun performSearch() {
        searchJob?.cancel()
        if (currentQuery.isBlank()) {
            _uiState.value = SearchUiState.Success(emptyList())
            return
        }

        searchJob = viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            try {
                delay(300) // Debounce search
                val languageQuery = _selectedLanguage?.let { "language:$it" } ?: ""
                val sortQuery = when (_sortBy) {
                    SortBy.STARS -> "stars"
                    SortBy.UPDATED -> "updated"
                }
                val fullQuery = "$currentQuery $languageQuery sort:$sortQuery"
                val response = repository.searchRepositories(fullQuery)
                _uiState.value = SearchUiState.Success(response.items)
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(e.message ?: "Search failed")
            }
        }
    }
}

sealed class SearchUiState {
    object Loading : SearchUiState()
    data class Success(val repositories: List<Repository>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
} 