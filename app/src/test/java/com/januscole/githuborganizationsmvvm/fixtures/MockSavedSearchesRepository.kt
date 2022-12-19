package com.januscole.githuborganizationsmvvm.fixtures

import com.januscole.githuborganizationsmvvm.data.searches.repository.SearchCriteriaDatastoreRepository
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData.Companion.VALID_SEARCH_TERM_1
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData.Companion.VALID_SEARCH_TERM_2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockSavedSearchesRepository : SearchCriteriaDatastoreRepository {

    private val recentSearches = mutableListOf(VALID_SEARCH_TERM_1, VALID_SEARCH_TERM_2)

    override suspend fun addSearch(searchCriteria: String) {
        if (!recentSearches.contains(searchCriteria)) {
            recentSearches.add(searchCriteria)
        }
    }

    override suspend fun savedSearches(): Flow<List<String>> = flow {
        emit(recentSearches)
    }
}
