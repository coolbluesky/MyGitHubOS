package com.example.mygithubos.domain.usecase

import com.example.mygithubos.data.model.SearchResponse
import com.example.mygithubos.domain.repository.GitHubRepository
import javax.inject.Inject

class SearchRepositoriesUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(
        query: String,
        language: String? = null,
        sort: String = "stars",
        order: String = "desc",
        page: Int = 1,
        perPage: Int = 30
    ): SearchResponse {
        // 构建搜索查询
        val searchQuery = buildString {
            append(query)
            if (!language.isNullOrBlank()) {
                append(" language:$language")
            }
        }
        
        return repository.searchRepositories(
            query = searchQuery,
            sort = sort,
            order = order,
            page = page,
            perPage = perPage
        )
    }
} 