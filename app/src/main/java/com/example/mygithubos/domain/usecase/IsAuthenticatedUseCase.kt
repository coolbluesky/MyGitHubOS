package com.example.mygithubos.domain.usecase

import com.example.mygithubos.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsAuthenticatedUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.isAuthenticated()
} 