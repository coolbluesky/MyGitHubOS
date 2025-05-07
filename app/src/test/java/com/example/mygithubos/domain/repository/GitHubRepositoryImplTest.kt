package com.example.mygithubos.domain.repository

import com.example.mygithubos.data.api.GitHubApi
import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.data.model.User
import com.example.mygithubos.data.repository.GitHubRepositoryImpl
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GitHubRepositoryImplTest {
    private lateinit var repository: GitHubRepositoryImpl
    private lateinit var api: GitHubApi

    @Before
    fun setup() {
        api = mock()
        repository = GitHubRepositoryImpl(api)
    }

    @Test
    fun `when user is not authenticated, should return false`() = runTest {
        whenever(api.getCurrentUser()).thenThrow(RuntimeException("Not authenticated"))
        assertFalse(repository.isAuthenticated())
    }

    @Test
    fun `when user is authenticated, should return true`() = runTest {
        val mockUser = User(login = "testuser", id = 1)
        whenever(api.getCurrentUser()).thenReturn(mockUser)
        assertTrue(repository.isAuthenticated())
    }

    @Test
    fun `when search repositories, should return correct results`() = runTest {
        val query = "kotlin"
        val language = "Kotlin"
        val mockRepositories = listOf(
            Repository(
                id = 1,
                name = "test-repo",
                fullName = "owner/test-repo",
                description = "Test repository",
                owner = User(login = "owner", id = 1),
                stargazersCount = 100,
                language = "Kotlin"
            )
        )

        whenever(api.searchRepositories(query, language)).thenReturn(mockRepositories)
        val result = repository.searchRepositories(query, language)
        assertEquals(mockRepositories, result)
        verify(api).searchRepositories(query, language)
    }

    @Test
    fun `when get current user, should return user data`() = runTest {
        val mockUser = User(
            login = "testuser",
            id = 1,
            bio = "Test bio",
            followers = 100,
            following = 50,
            publicRepos = 20
        )

        whenever(api.getCurrentUser()).thenReturn(mockUser)
        val result = repository.getCurrentUser()
        assertEquals(mockUser, result)
        verify(api).getCurrentUser()
    }

    @Test
    fun `when get user repositories, should return repositories list`() = runTest {
        val username = "testuser"
        val mockRepositories = listOf(
            Repository(
                id = 1,
                name = "test-repo",
                fullName = "testuser/test-repo",
                description = "Test repository",
                owner = User(login = username, id = 1),
                stargazersCount = 100,
                language = "Kotlin"
            )
        )

        whenever(api.getUserRepositories(username)).thenReturn(mockRepositories)
        val result = repository.getUserRepositories(username)
        assertEquals(mockRepositories, result)
        verify(api).getUserRepositories(username)
    }

    @Test
    fun `when get repository, should return repository details`() = runTest {
        val owner = "testuser"
        val repoName = "test-repo"
        val mockRepo = Repository(
            id = 1,
            name = repoName,
            fullName = "$owner/$repoName",
            description = "Test repository",
            owner = User(login = owner, id = 1),
            stargazersCount = 100,
            language = "Kotlin"
        )

        whenever(api.getRepository(owner, repoName)).thenReturn(mockRepo)
        val result = repository.getRepository(owner, repoName)
        assertEquals(mockRepo, result)
        verify(api).getRepository(owner, repoName)
    }
} 