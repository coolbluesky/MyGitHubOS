package com.example.mygithubos.data.repository

import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.data.model.SearchResponse
import com.example.mygithubos.data.model.User

interface GitHubRepository {
    suspend fun searchRepositories(
        query: String,
        language: String? = null,
        sort: String = "stars",
        order: String = "desc",
        page: Int = 1,
        perPage: Int = 30
    ): SearchResponse

    suspend fun getRepository(owner: String, repo: String): Repository

    suspend fun getCurrentUser(): User

    suspend fun login(token: String): User

    fun isAuthenticated(): Boolean

    fun getAuthToken(): String?
} 