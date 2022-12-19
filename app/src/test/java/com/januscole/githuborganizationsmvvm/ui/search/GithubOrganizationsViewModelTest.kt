package com.januscole.githuborganizationsmvvm.ui.search

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.januscole.githuborganizationsmvvm.data.github.organizations.datasource.model.toGithubOrganization
import com.januscole.githuborganizationsmvvm.data.github.organizations.repository.OrganizationRepository
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData.Companion.VALID_SEARCH_CRITERIA
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData.Companion.VALID_SEARCH_TERM_1
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData.Companion.VALID_SEARCH_TERM_2
import com.januscole.githuborganizationsmvvm.fixtures.MockSavedSearchesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class GithubOrganizationsViewModelTest {

    // Mocks
    private var mockOrganizationRepository: OrganizationRepository = Mockito.mock(
        OrganizationRepository::class.java
    )

    private var mockSavedStateHandle: SavedStateHandle = SavedStateHandle().apply {
        set(SAVED_SEARCHES, listOf<String>())
        set(UI_STATE, OrganizationSearchViewModel.GithubOrganizationUiState())
    }

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
    fun `Fetching Valid Organization Is Successful`() = runTest {
        // Setup
        val githubOrganizationViewModel = OrganizationSearchViewModel(mockOrganizationRepository, MockSavedSearchesRepository(), mockSavedStateHandle)
        Mockito.`when`(mockOrganizationRepository.getGithubOrganization(VALID_SEARCH_CRITERIA)).thenReturn(
            Result.success(MockGithubData().getMockGithubOrganization().toGithubOrganization())
        )

        // Execute The Test
        githubOrganizationViewModel.fetchOrganization(VALID_SEARCH_CRITERIA)

        // Result
        val job = launch {
            githubOrganizationViewModel.uiState.test {
                val result = awaitItem()
                assertEquals(MockGithubData().getMockGithubOrganization().toGithubOrganization(), result.organization)
                assertNull(result.exception)
                assertFalse(result.isLoading)
            }
        }
        advanceTimeBy(500)
        job.cancel()
    }

    @Test
    fun `Fetching Invalid Organization Returns Error UI State`() = runTest {
        // Setup
        val githubOrganizationViewModel = OrganizationSearchViewModel(mockOrganizationRepository, MockSavedSearchesRepository(), mockSavedStateHandle)
        Mockito.`when`(mockOrganizationRepository.getGithubOrganization(anyString())).thenReturn(
            Result.failure(Exception("ERROR MESSAGE"))
        )

        // Execute The Test
        githubOrganizationViewModel.fetchOrganization("Nonsense")

        // Result
        val job = launch {
            githubOrganizationViewModel.uiState.test {
                val result = awaitItem()
                assertNull(result.organization)
                assertNotNull(result.exception)
                assertFalse(result.isLoading)
            }
        }
        advanceTimeBy(500)
        job.cancel()
    }

    @Test
    fun `Consuming Search Event initializes UI State`() = runTest {
        // Setup
        val githubOrganizationViewModel = OrganizationSearchViewModel(mockOrganizationRepository, MockSavedSearchesRepository(), mockSavedStateHandle)
        Mockito.`when`(mockOrganizationRepository.getGithubOrganization(VALID_SEARCH_CRITERIA)).thenReturn(
            Result.success(MockGithubData().getMockGithubOrganization().toGithubOrganization())
        )

        // Execute The Test
        githubOrganizationViewModel.fetchOrganization(VALID_SEARCH_CRITERIA)

        // Result
        val job = launch {
            githubOrganizationViewModel.uiState.test {
                val result = awaitItem()
                assertEquals(MockGithubData().getMockGithubOrganization().toGithubOrganization(), result.organization)
                assertNull(result.exception)
                assertFalse(result.isLoading)
                val consumeSearchEvent = awaitItem()
                assertNull(consumeSearchEvent.organization)
                assertNull(consumeSearchEvent.exception)
                assertFalse(consumeSearchEvent.isLoading)
            }
        }
        advanceTimeBy(500)
        githubOrganizationViewModel.consumeSearchEvent()
        advanceTimeBy(500)
        job.cancel()
    }

    @Test
    fun `Fetching Organization Sets IsLoading To True While Loading`() = runTest {
        // Setup
        val githubOrganizationViewModel = OrganizationSearchViewModel(mockOrganizationRepository, MockSavedSearchesRepository(), mockSavedStateHandle)
        Mockito.`when`(mockOrganizationRepository.getGithubOrganization(VALID_SEARCH_CRITERIA)).thenReturn(
            Result.success(MockGithubData().getMockGithubOrganization().toGithubOrganization())
        )

        val job = launch {
            githubOrganizationViewModel.uiState.test {
                val initialState = awaitItem()
                assertFalse(initialState.isLoading)
                val loadingState = awaitItem()
                assertTrue(loadingState.isLoading)
                val resultState = awaitItem()
                assertFalse(resultState.isLoading)
            }
        }

        // Execute The Test
        githubOrganizationViewModel.fetchOrganization(VALID_SEARCH_CRITERIA)

        advanceTimeBy(500)
        job.cancel()
    }

    @Test
    fun `Organization ViewModel Returns List Of Saved Searches`() = runTest {

        // Execute The Test
        val githubOrganizationViewModel = OrganizationSearchViewModel(mockOrganizationRepository, MockSavedSearchesRepository(), mockSavedStateHandle)

        // Result
        val job = launch {
            githubOrganizationViewModel.savedSearches.test {
                val savedSearches = awaitItem()
                assertEquals(savedSearches.size, 2)
                assertEquals(savedSearches[0], VALID_SEARCH_TERM_1)
                assertEquals(savedSearches[1], VALID_SEARCH_TERM_2)
            }
        }
        advanceTimeBy(500)
        job.cancel()
    }

    @Test
    fun `Organization ViewModel Does Not Update List Of Saved Searches For Existing Search Terms`() = runTest {

        // Setup
        val githubOrganizationViewModel = OrganizationSearchViewModel(mockOrganizationRepository, MockSavedSearchesRepository(), mockSavedStateHandle)
        Mockito.`when`(mockOrganizationRepository.getGithubOrganization(anyString())).thenReturn(
            Result.success(MockGithubData().getMockGithubOrganization().toGithubOrganization())
        )

        // Execute The Test
        githubOrganizationViewModel.fetchOrganization(VALID_SEARCH_TERM_1)

        // Result
        val job = launch {
            githubOrganizationViewModel.savedSearches.test {
                val savedSearches = awaitItem()
                assertEquals(savedSearches.size, 2)
                assertEquals(savedSearches[0], VALID_SEARCH_TERM_1)
                assertEquals(savedSearches[1], VALID_SEARCH_TERM_2)
            }
        }
        advanceTimeBy(500)
        job.cancel()
    }
}
