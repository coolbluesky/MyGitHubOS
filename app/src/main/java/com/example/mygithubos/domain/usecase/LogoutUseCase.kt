package com.example.mygithubos.domain.usecase

import com.example.mygithubos.domain.repository.GitHubRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke() = repository.logout()
} 