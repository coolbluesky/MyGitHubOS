package com.example.mygithubos.ui.screens.repo_details

import app.cash.turbine.test
import com.example.mygithubos.domain.repository.GitHubRepository
import com.example.mygithubos.data.model.Repository
import com.example.mygithubos.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class RepoDetailsViewModelTest {
    private lateinit var viewModel: RepoDetailsViewModel
    private lateinit var repository: GitHubRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = RepoDetailsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when load repository details succeeds, should show repository data`() = runTest {
        val owner = "testuser"
        val repoName = "test-repo"
        val mockRepo = Repository(
            id = 1,
            name = repoName,
            fullName = "$owner/$repoName",
            description = "Test repository",
            owner = User(login = owner, id = 1),
            stargazersCount = 100,
            language = "Kotlin",
            forksCount = 50,
            openIssuesCount = 10,
            watchersCount = 200,
            defaultBranch = "main",
            visibility = "public",
            createdAt = "2023-01-01T00:00:00Z",
            updatedAt = "2023-01-02T00:00:00Z"
        )

        whenever(repository.getRepository(owner, repoName)).thenReturn(mockRepo)

        viewModel.loadRepositoryDetails(owner, repoName)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is RepoDetailsUiState.Loading)

            val successState = awaitItem()
            assertTrue(successState is RepoDetailsUiState.Success)
            assertEquals(mockRepo, (successState as RepoDetailsUiState.Success).repository)
        }

        verify(repository).getRepository(owner, repoName)
    }

    @Test
    fun `when load repository details fails, should show error state`() = runTest {
        val owner = "testuser"
        val repoName = "test-repo"
        val errorMessage = "Failed to load repository"
        whenever(repository.getRepository(owner, repoName)).thenThrow(RuntimeException(errorMessage))

        viewModel.loadRepositoryDetails(owner, repoName)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is RepoDetailsUiState.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is RepoDetailsUiState.Error)
            assertEquals(errorMessage, (errorState as RepoDetailsUiState.Error).message)
        }
    }
} 