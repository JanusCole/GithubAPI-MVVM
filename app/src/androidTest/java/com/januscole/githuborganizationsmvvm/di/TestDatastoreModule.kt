package com.januscole.githuborganizationsmvvm.di

import com.januscole.githuborganizationsmvvm.data.searches.repository.SearchCriteriaDatastoreRepository
import com.januscole.githuborganizationsmvvm.fixtures.MockSavedSearchesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TestDatastoreModule {

    @Singleton
    @Provides
    fun provideSearchCriteriaDatastoreRepository(): SearchCriteriaDatastoreRepository {
        return MockSavedSearchesRepository
    }
}
