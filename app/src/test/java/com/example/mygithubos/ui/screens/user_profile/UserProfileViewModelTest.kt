package com.example.mygithubos.ui.screens.user_profile

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
class UserProfileViewModelTest {
    private lateinit var viewModel: UserProfileViewModel
    private lateinit var repository: GitHubRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = UserProfileViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when load profile succeeds, should show user data and repositories`() = runTest {
        val mockUser = User(
            login = "testuser",
            id = 1,
            bio = "Test bio",
            followers = 100,
            following = 50,
            publicRepos = 20
        )
        val mockRepositories = listOf(
            Repository(
                id = 1,
                name = "test-repo",
                fullName = "testuser/test-repo",
                description = "Test repository",
                owner = mockUser,
                stargazersCount = 100,
                language = "Kotlin"
            )
        )

        whenever(repository.getCurrentUser()).thenReturn(mockUser)
        whenever(repository.getUserRepositories("testuser")).thenReturn(mockRepositories)

        viewModel.loadUserProfile()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is UserProfileUiState.Loading)

            val successState = awaitItem()
            assertTrue(successState is UserProfileUiState.Success)
            assertEquals(mockUser, (successState as UserProfileUiState.Success).user)
            assertEquals(mockRepositories, successState.repositories)
        }

        verify(repository).getCurrentUser()
        verify(repository).getUserRepositories("testuser")
    }

    @Test
    fun `when load profile fails, should show error state`() = runTest {
        val errorMessage = "Failed to load profile"
        whenever(repository.getCurrentUser()).thenThrow(RuntimeException(errorMessage))

        viewModel.loadUserProfile()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is UserProfileUiState.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is UserProfileUiState.Error)
            assertEquals(errorMessage, (errorState as UserProfileUiState.Error).message)
        }
    }

    @Test
    fun `when logout is called, should show logged out state`() = runTest {
        viewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val loggedOutState = awaitItem()
            assertTrue(loggedOutState is UserProfileUiState.LoggedOut)
        }

        verify(repository).logout()
    }
} 