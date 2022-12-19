package com.januscole.githuborganizationsmvvm.fixtures

import com.januscole.githuborganizationsmvvm.data.searches.repository.SearchCriteriaDatastoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object MockSavedSearchesRepository : SearchCriteriaDatastoreRepository {

    private val recentSearches = mutableListOf("cantina", "nytimes")

    override suspend fun addSearch(searchCriteria: String) {
        if (!recentSearches.contains(searchCriteria)) {
            recentSearches.add(searchCriteria)
        }
    }

    override suspend fun savedSearches(): Flow<List<String>> = flow {
        emit(recentSearches)
    }
}
