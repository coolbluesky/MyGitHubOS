package com.example.mygithubos.data.repository

import com.example.mygithubos.data.api.GitHubApi
import com.example.mygithubos.data.auth.GitHubOAuthService
import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.data.model.SearchResponse
import com.example.mygithubos.data.model.User
import com.example.mygithubos.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubRepositoryImpl @Inject constructor(
    private val api: GitHubApi,
    private val oauthService: GitHubOAuthService
) : GitHubRepository {

    private val _isAuthenticated = MutableStateFlow(false)
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
        return api.getCurrentUser()
    }

    override suspend fun login(token: String): User {
        authToken = token
        _isAuthenticated.value = true
        return getCurrentUser()
    }

    override fun isAuthenticated(): Flow<Boolean> = _isAuthenticated.asStateFlow()

    override fun getAuthToken(): String? = authToken

    override suspend fun getUserRepositories(
        page: Int,
        perPage: Int
    ): List<Repository> {
        return api.getUserRepositories(page, perPage)
    }
} 