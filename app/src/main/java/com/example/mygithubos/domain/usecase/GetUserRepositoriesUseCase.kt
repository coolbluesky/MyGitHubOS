package com.example.mygithubos.domain.usecase

import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.domain.repository.GitHubRepository
import javax.inject.Inject

class GetUserRepositoriesUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(
        page: Int = 1,
        perPage: Int = 30
    ): List<Repository> = repository.getUserRepositories(page, perPage)
} 