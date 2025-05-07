package com.example.mygithubos.ui.screens.search

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
class SearchViewModelTest {
    private lateinit var viewModel: SearchViewModel
    private lateinit var repository: GitHubRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = SearchViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when search query is empty, should not trigger search`() = runTest {
        viewModel.searchRepositories("")
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState is SearchUiState.Success)
            assertEquals(emptyList(), (initialState as SearchUiState.Success).repositories)
        }
    }

    @Test
    fun `when search query changes, should update results`() = runTest {
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

        whenever(repository.searchRepositories("test")).thenReturn(mockRepositories)

        viewModel.searchRepositories("test")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is SearchUiState.Loading)

            val successState = awaitItem()
            assertTrue(successState is SearchUiState.Success)
            assertEquals(mockRepositories, (successState as SearchUiState.Success).repositories)
        }

        verify(repository).searchRepositories("test")
    }

    @Test
    fun `when language filter changes, should update results`() = runTest {
        val mockRepositories = listOf(
            Repository(
                id = 1,
                name = "kotlin-repo",
                fullName = "owner/kotlin-repo",
                description = "Kotlin repository",
                owner = User(login = "owner", id = 1),
                stargazersCount = 100,
                language = "Kotlin"
            )
        )

        whenever(repository.searchRepositories("test", language = "Kotlin")).thenReturn(mockRepositories)

        viewModel.setLanguage("Kotlin")
        viewModel.searchRepositories("test")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is SearchUiState.Loading)

            val successState = awaitItem()
            assertTrue(successState is SearchUiState.Success)
            assertEquals(mockRepositories, (successState as SearchUiState.Success).repositories)
        }

        verify(repository).searchRepositories("test", language = "Kotlin")
    }

    @Test
    fun `when search fails, should show error state`() = runTest {
        val errorMessage = "Network error"
        whenever(repository.searchRepositories("test")).thenThrow(RuntimeException(errorMessage))

        viewModel.searchRepositories("test")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is SearchUiState.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is SearchUiState.Error)
            assertEquals(errorMessage, (errorState as SearchUiState.Error).message)
        }
    }
} 