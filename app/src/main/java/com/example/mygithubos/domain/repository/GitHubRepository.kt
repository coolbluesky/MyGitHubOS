package com.example.mygithubos.domain.repository

import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.data.model.SearchResponse
import com.example.mygithubos.data.model.User
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {
    suspend fun searchRepositories(
        query: String,
        sort: String = "stars",
        order: String = "desc",
        page: Int = 1,
        perPage: Int = 30
    ): SearchResponse

    suspend fun getRepository(owner: String, repo: String): Repository

    suspend fun getCurrentUser(): User

    suspend fun getUserRepositories(
        page: Int = 1,
        perPage: Int = 30
    ): List<Repository>
} 