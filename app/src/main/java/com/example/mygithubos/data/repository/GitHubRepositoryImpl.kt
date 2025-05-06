package com.example.mygithubos.data.repository

import com.example.mygithubos.data.api.GitHubApi
import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.data.model.SearchResponse
import com.example.mygithubos.data.model.User
import com.example.mygithubos.domain.repository.GitHubRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubRepositoryImpl @Inject constructor(
    private val api: GitHubApi
) : GitHubRepository {

    private var authToken: String? = null

    override suspend fun searchRepositories(
        query: String,
        language: String?,
        sort: String,
        order: String,
        page: Int,
        perPage: Int
    ): SearchResponse {
        return api.searchRepositories(query, language, sort, order, page, perPage)
    }

    override suspend fun getRepository(owner: String, repo: String): Repository {
        return api.getRepository(owner, repo)
    }

    override suspend fun getCurrentUser(): User {
        return api.getCurrentUser(authToken)
    }

    override suspend fun login(token: String): User {
        authToken = token
        return getCurrentUser()
    }

    override fun isAuthenticated(): Boolean {
        return authToken != null
    }

    override fun getAuthToken(): String? {
        return authToken
    }

    override suspend fun getUserRepositories(
        page: Int,
        perPage: Int
    ): List<Repository> {
        return api.getUserRepositories(page, perPage)
    }
} 