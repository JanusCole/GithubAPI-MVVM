package com.januscole.githuborganizationsmvvm.di

import com.januscole.githuborganizationsmvvm.data.github.organizations.repository.OrganizationRepository
import com.januscole.githuborganizationsmvvm.data.github.organizations.repository.OrganizationRepositoryImpl
import com.januscole.githuborganizationsmvvm.data.github.organizations.service.OrganizationSearchService
import com.januscole.githuborganizationsmvvm.data.github.repositories.repository.RepositoriesRepository
import com.januscole.githuborganizationsmvvm.data.github.repositories.repository.RepositoriesRepositoryImpl
import com.januscole.githuborganizationsmvvm.data.github.repositories.service.RepositoriesSearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Singleton
    @Provides
    fun provideGithubOrganizationRepository(
        githubOrganizationSearchService: OrganizationSearchService
    ): OrganizationRepository {
        return OrganizationRepositoryImpl(githubOrganizationSearchService)
    }

    @Singleton
    @Provides
    fun provideGithubReposRepository(
        githubReposService: RepositoriesSearchService
    ): RepositoriesRepository {
        return RepositoriesRepositoryImpl(githubReposService)
    }
}
