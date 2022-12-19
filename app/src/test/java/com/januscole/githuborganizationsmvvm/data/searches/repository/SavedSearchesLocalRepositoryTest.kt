package com.januscole.githuborganizationsmvvm.data.searches.repository

import androidx.datastore.core.DataStore
import com.januscole.githuborganizationsmvvm.SavedSearches
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData.Companion.VALID_SEARCH_TERM_1
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData.Companion.VALID_SEARCH_TERM_2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

@Suppress("UNCHECKED_CAST")
@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class SavedSearchesLocalRepositoryTest {

    // Class under test
    private var githubSavedSearchesRepository: SearchCriteriaDatastoreRepository? = null

    @Mock
    private var mockSavedSearchesDataStore: DataStore<SavedSearches> = mock()
    private var mockSavedSearches: SavedSearches = mock(SavedSearches::class.java)

    @Test
    fun `Saved Searches Repository Exposes Saved Searches From The DataStore`() = runTest {
        // Setup
        `when`(mockSavedSearches.searchCriteriaList).thenReturn(listOf(VALID_SEARCH_TERM_1, VALID_SEARCH_TERM_2))
        `when`(mockSavedSearchesDataStore.data).thenReturn(
            flow {
                emit(mockSavedSearches)
            }
        )

        // Execute the test
        val githubSavedSearchesRepository = SavedSearchesLocalRepository(mockSavedSearchesDataStore)

        // Results
        val job = launch {
            githubSavedSearchesRepository.savedSearches().collect {
                assertEquals(it.size, 2)
                assertEquals(it[0], VALID_SEARCH_TERM_1)
                assertEquals(it[1], VALID_SEARCH_TERM_2)
            }
        }
        advanceTimeBy(500)
        job.cancel()
    }

    @Test
    fun `Adding A Search Criteria Calls The UpdateData Function`() = runTest {
        // Setup
        githubSavedSearchesRepository = SavedSearchesLocalRepository(mockSavedSearchesDataStore)

        // Execute the test
        githubSavedSearchesRepository!!.addSearch(MockGithubData.VALID_SEARCH_CRITERIA)

        // Verify the number of invocations of the updateData function
        verify(mockSavedSearchesDataStore)?.updateData(any())
        // This is fine. But maybe consider a more specific verification of the lambdas called
    }

    // This is a cute thing I found on the internet that mocks "any" lambda for testing
    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }
}
