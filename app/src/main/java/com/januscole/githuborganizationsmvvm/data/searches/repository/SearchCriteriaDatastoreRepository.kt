package com.januscole.githuborganizationsmvvm.data.searches.repository

import kotlinx.coroutines.flow.Flow

interface SearchCriteriaDatastoreRepository {
    suspend fun addSearch(searchCriteria: String)
    suspend fun savedSearches(): Flow<List<String>>
}
