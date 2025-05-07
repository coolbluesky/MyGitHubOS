package com.example.mygithubos.di

import android.content.Context
import com.example.mygithubos.data.api.GitHubApi
import com.example.mygithubos.data.auth.GitHubOAuthService
import com.example.mygithubos.data.repository.GitHubRepositoryImpl
import com.example.mygithubos.domain.repository.GitHubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGitHubRepository(
        api: GitHubApi,
        oauthService: GitHubOAuthService,
        @ApplicationContext context: Context
    ): GitHubRepository {
        return GitHubRepositoryImpl(api, oauthService, context)
    }
} 