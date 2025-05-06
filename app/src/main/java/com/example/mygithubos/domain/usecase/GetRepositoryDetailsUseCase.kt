package com.example.mygithubos.domain.usecase

import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.domain.repository.GitHubRepository
import javax.inject.Inject

class GetRepositoryDetailsUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(owner: String, repo: String): Repository {
        return repository.getRepository(owner, repo)
    }
} 