package com.example.mygithubos.domain.usecase

import com.example.mygithubos.data.model.User
import com.example.mygithubos.domain.repository.GitHubRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(): User {
        return repository.getCurrentUser()
    }
} 