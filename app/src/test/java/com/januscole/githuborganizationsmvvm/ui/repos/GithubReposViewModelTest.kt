package com.januscole.githuborganizationsmvvm.ui.repos

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.januscole.githuborganizationsmvvm.data.github.organizations.datasource.model.toGithubOrganization
import com.januscole.githuborganizationsmvvm.data.github.organizations.repository.OrganizationRepository
import com.januscole.githuborganizationsmvvm.data.github.repositories.datasource.model.toGithubOrganizationRepos
import com.januscole.githuborganizationsmvvm.data.github.repositories.repository.RepositoriesRepository
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData.Companion.VALID_SEARCH_CRITERIA
import com.januscole.githuborganizationsmvvm.ui.search.UI_STATE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class GithubReposViewModelTest {

    // Mocks
    private var mockOrganizationRepository: OrganizationRepository = Mockito.mock(
        OrganizationRepository::class.java
    )

    private var mockReposRepository: RepositoriesRepository = Mockito.mock(RepositoriesRepository::class.java)

    private var mockSavedStateHandle: SavedStateHandle = SavedStateHandle().apply {
        set(UI_STATE, OrganizationRepositoriesViewModel.GithubReposUiState())
    }

    // Class under test
    private var githubReposViewModel: OrganizationRepositoriesViewModel = OrganizationRepositoriesViewModel(mockOrganizationRepository, mockReposRepository, mockSavedStateHandle)

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Fetch Valid Organization Repos Is Successful`() = runTest {

        // Setup
        Mockito.`when`(mockOrganizationRepository.getGithubOrganization(VALID_SEARCH_CRITERIA)).thenReturn(
            Result.success(MockGithubData().getMockGithubOrganization().toGithubOrganization())
        )
        Mockito.`when`(mockReposRepository.getGithubRepos(VALID_SEARCH_CRITERIA)).thenReturn(
            Result.success(MockGithubData().getMockGithubOrganizationRepos().toGithubOrganizationRepos())
        )

        // Execute The Test
        githubReposViewModel.fetchRepos(VALID_SEARCH_CRITERIA)

        // Results
        val job = launch {
            githubReposViewModel.uiState.test {
                val result = awaitItem()
                Assert.assertEquals(MockGithubData().getMockGithubOrganization().toGithubOrganization(), result.organization)
                Assert.assertEquals(MockGithubData().getMockGithubOrganizationRepos().toGithubOrganizationRepos(), result.repos)
                Assert.assertNull(result.exception)
                Assert.assertFalse(result.isLoading)
            }
        }
        advanceTimeBy(500)
        job.cancel()
    }

    @Test
    fun `Fetch Invalid Organization Returns Error UI State`() = runTest {

        // Setup
        Mockito.`when`(mockOrganizationRepository.getGithubOrganization(ArgumentMatchers.anyString())).thenReturn(
            Result.failure(Exception("ERROR MESSAGE"))
        )

        // Execute The Test
        githubReposViewModel.fetchRepos("Nonsense")

        // Results
        val job = launch {
            githubReposViewModel.uiState.test {
                val result = awaitItem()
                Assert.assertNull(result.organization)
                Assert.assertNull(result.repos)
                Assert.assertNotNull(result.exception)
                Assert.assertFalse(result.isLoading)
            }
        }
        advanceTimeBy(500)
        job.cancel()
    }

    @Test
    fun `Fetch Invalid Repos Returns Error UI State`() = runTest {

        // Setup
        Mockito.`when`(mockReposRepository.getGithubRepos(ArgumentMatchers.anyString())).thenReturn(
            Result.failure(Exception("ERROR MESSAGE"))
        )

        // Execute The Test
        githubReposViewModel.fetchRepos(VALID_SEARCH_CRITERIA)

        // Results
        val job = launch {
            githubReposViewModel.uiState.test {
                val result = awaitItem()
                Assert.assertNull(result.repos)
                Assert.assertNotNull(result.exception)
                Assert.assertFalse(result.isLoading)
            }
        }
        advanceTimeBy(500)
        job.cancel()
    }

    @Test
    fun `Fetching Repos Sets IsLoading To True While Loading`() = runTest {

        val job = launch {
            githubReposViewModel.uiState.test {
                val initialState = awaitItem()
                Assert.assertFalse(initialState.isLoading)
                val loadingState = awaitItem()
                Assert.assertTrue(loadingState.isLoading)
                val resultState = awaitItem()
                Assert.assertFalse(resultState.isLoading)
            }
        }

        // Execute The Test
        githubReposViewModel.fetchRepos(VALID_SEARCH_CRITERIA)

        advanceTimeBy(500)
        job.cancel()
    }

    @Test
    fun `Consuming Search Event initializes UI State`() = runTest {

        // Setup
        Mockito.`when`(mockOrganizationRepository.getGithubOrganization(VALID_SEARCH_CRITERIA)).thenReturn(
            Result.success(MockGithubData().getMockGithubOrganization().toGithubOrganization())
        )
        Mockito.`when`(mockReposRepository.getGithubRepos(VALID_SEARCH_CRITERIA)).thenReturn(
            Result.success(MockGithubData().getMockGithubOrganizationRepos().toGithubOrganizationRepos())
        )

        // Execute The Test
        githubReposViewModel.fetchRepos(VALID_SEARCH_CRITERIA)

        // Results
        val job = launch {
            githubReposViewModel.uiState.test {
                val result = awaitItem()
                Assert.assertEquals(
                    MockGithubData().getMockGithubOrganizationRepos().toGithubOrganizationRepos(),
                    result.repos
                )
                Assert.assertNull(result.exception)
                Assert.assertFalse(result.isLoading)
                val consumeSearchEvent = awaitItem()
                Assert.assertNull(consumeSearchEvent.organization)
                Assert.assertNull(consumeSearchEvent.repos)
                Assert.assertNull(consumeSearchEvent.exception)
                Assert.assertFalse(consumeSearchEvent.isLoading)
            }
        }
        advanceTimeBy(500)
        githubReposViewModel.consumeSearchEvent()
        advanceTimeBy(500)
        job.cancel()
    }
}
