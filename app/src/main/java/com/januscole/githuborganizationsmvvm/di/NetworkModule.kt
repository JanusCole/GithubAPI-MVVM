package com.januscole.githuborganizationsmvvm.di

import com.januscole.githuborganizationsmvvm.BuildConfig
import com.januscole.githuborganizationsmvvm.data.github.organizations.datasource.GithubOrganizationRetrofitSearch
import com.januscole.githuborganizationsmvvm.data.github.organizations.service.OrganizationSearchService
import com.januscole.githuborganizationsmvvm.data.github.organizations.service.OrganizationSearchServiceImpl
import com.januscole.githuborganizationsmvvm.data.github.repositories.datasource.GithubReposRetrofitSearch
import com.januscole.githuborganizationsmvvm.data.github.repositories.service.RepositoriesSearchService
import com.januscole.githuborganizationsmvvm.data.github.repositories.service.RepositoriesSearchServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideGithubOrganizationSearchService(): OrganizationSearchService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_GITHUB_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubOrganizationRetrofitSearch::class.java)

        return OrganizationSearchServiceImpl(retrofit)
    }

    @Singleton
    @Provides
    fun provideGithubReposSearchService(): RepositoriesSearchService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_GITHUB_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubReposRetrofitSearch::class.java)

        return RepositoriesSearchServiceImpl(retrofit)
    }
}
