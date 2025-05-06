package com.example.mygithubos.data.repository

import com.example.mygithubos.data.api.GitHubApi
import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.data.model.SearchResponse
import com.example.mygithubos.data.model.User
import com.example.mygithubos.domain.repository.GitHubRepository
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(
    private val api: GitHubApi
) : GitHubRepository {

    override suspend fun searchRepositories(
        query: String,
        sort: String,
        order: String,
        page: Int,
        perPage: Int
    ): SearchResponse {
        return api.searchRepositories(query, sort, order, page, perPage)
    }

    override suspend fun getRepository(owner: String, repo: String): Repository {
        return api.getRepository(owner, repo)
    }

    override suspend fun getCurrentUser(): User {
        return api.getCurrentUser()
    }

    override suspend fun getUserRepositories(
        page: Int,
        perPage: Int
    ): List<Repository> {
        return api.getUserRepositories(page, perPage)
    }
} 