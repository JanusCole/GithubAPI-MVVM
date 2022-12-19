package com.januscole.githuborganizationsmvvm.data.searches.repository

import androidx.datastore.core.DataStore
import com.januscole.githuborganizationsmvvm.SavedSearches
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SavedSearchesLocalRepository(private val savedSearchesStore: DataStore<SavedSearches>) : SearchCriteriaDatastoreRepository {

    private val recentSearches = mutableListOf<String>()

    override suspend fun savedSearches(): Flow<List<String>> =
        flow {
            savedSearchesStore.data.collect {
                recentSearches.clear()
                recentSearches.addAll(it.searchCriteriaList)
                emit(it.searchCriteriaList)
            }
        }

    override suspend fun addSearch(searchCriteria: String) {
        if (!recentSearches.contains(searchCriteria)) {
            updateSavedSearches(searchCriteria)
        }
    }

    private suspend fun updateSavedSearches(searchCriteria: String) {
        savedSearchesStore.updateData { savedSearches ->
            savedSearches.toBuilder()
                .addSearchCriteria(searchCriteria)
                .build()
        }
    }
}
