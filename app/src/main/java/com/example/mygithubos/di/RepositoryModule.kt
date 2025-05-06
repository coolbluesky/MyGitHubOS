package com.example.mygithubos.di

import com.example.mygithubos.data.repository.GitHubRepositoryImpl
import com.example.mygithubos.domain.repository.GitHubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGitHubRepository(
        repositoryImpl: GitHubRepositoryImpl
    ): GitHubRepository
} 