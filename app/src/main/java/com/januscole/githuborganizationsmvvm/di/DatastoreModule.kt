package com.januscole.githuborganizationsmvvm.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.januscole.githuborganizationsmvvm.SavedSearches
import com.januscole.githuborganizationsmvvm.data.searches.repository.SavedSearchesLocalRepository
import com.januscole.githuborganizationsmvvm.data.searches.repository.SearchCriteriaDatastoreRepository
import com.januscole.githuborganizationsmvvm.datastore.SavedSearchesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatastoreModule {

    private const val DATA_STORE_FILE_NAME = "saved_searches.pb"

    // Build the DataStore
    private val Context.savedSearchesStore: DataStore<SavedSearches> by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = SavedSearchesSerializer
    )

    @Singleton
    @Provides
    fun provideSearchCriteriaDatastoreRepository(
        @ApplicationContext app: Context
    ): SearchCriteriaDatastoreRepository {
        return SavedSearchesLocalRepository(app.savedSearchesStore)
    }
}
