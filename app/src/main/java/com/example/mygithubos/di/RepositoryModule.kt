package com.example.mygithubos.di

import com.example.mygithubos.data.api.GitHubApi
import com.example.mygithubos.data.repository.GitHubRepositoryImpl
import com.example.mygithubos.domain.repository.GitHubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGitHubRepository(
        api: GitHubApi
    ): GitHubRepository {
        return GitHubRepositoryImpl(api)
    }
} 