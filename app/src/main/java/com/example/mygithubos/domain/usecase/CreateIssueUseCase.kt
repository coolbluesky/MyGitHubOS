package com.example.mygithubos.domain.usecase

import com.example.mygithubos.data.model.IssueRequest
import com.example.mygithubos.data.model.IssueResponse
import com.example.mygithubos.domain.repository.GitHubRepository
import javax.inject.Inject

class CreateIssueUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(
        owner: String,
        repo: String,
        issue: IssueRequest
    ): IssueResponse = repository.createIssue(owner, repo, issue)
} 