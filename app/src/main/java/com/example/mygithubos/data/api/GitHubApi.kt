package com.example.mygithubos.data.api

import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.data.model.SearchResponse
import com.example.mygithubos.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): SearchResponse

    @GET("repos/{owner}/{repo}")
    suspend fun getRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Repository

    @GET("user")
    suspend fun getCurrentUser(): User

    @GET("user/repos")
    suspend fun getUserRepositories(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): List<Repository>
} 