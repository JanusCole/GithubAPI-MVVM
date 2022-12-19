package com.januscole.githuborganizationsmvvm.di

import com.januscole.githuborganizationsmvvm.BuildConfig
import com.januscole.githuborganizationsmvvm.data.github.organizations.datasource.GithubOrganizationRetrofitSearch
import com.januscole.githuborganizationsmvvm.data.github.organizations.datasource.model.GithubOrganizationDTO
import com.januscole.githuborganizationsmvvm.data.github.organizations.service.OrganizationSearchService
import com.januscole.githuborganizationsmvvm.data.github.repositories.datasource.GithubReposRetrofitSearch
import com.januscole.githuborganizationsmvvm.data.github.repositories.datasource.model.GithubOrganizationRepoDTO
import com.januscole.githuborganizationsmvvm.data.github.repositories.service.RepositoriesSearchService
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object TestNetworkModule {
    @Singleton
    @Provides
    fun provideGithubOrganizationRetrofitSearch(): GithubOrganizationRetrofitSearch {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_GITHUB_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubOrganizationRetrofitSearch::class.java)
    }

    @Singleton
    @Provides
    fun provideGithubOrganizationSearchService(): OrganizationSearchService {
        return object : OrganizationSearchService {
            override suspend fun getGithubOrganization(organizationSearchId: String): GithubOrganizationDTO {
                if (organizationSearchId.contains(MockGithubData.VALID_SEARCH_CRITERIA)) {
                    return MockGithubData().getMockGithubOrganization()
                } else {
                    throw Exception("ERROR MESSAGE")
                }
            }
        }
    }

    @Singleton
    @Provides
    fun provideGithubReposRetrofitSearch(): GithubReposRetrofitSearch {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_GITHUB_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubReposRetrofitSearch::class.java)
    }

    @Singleton
    @Provides
    fun provideGithubReposSearchService(): RepositoriesSearchService {
        return object : RepositoriesSearchService {
            override suspend fun getGithubRepos(organizationSearchId: String): List<GithubOrganizationRepoDTO> {
                if (organizationSearchId.contains(MockGithubData.VALID_SEARCH_CRITERIA)) {
                    return MockGithubData().getMockGithubOrganizationRepos()
                } else {
                    throw Exception("ERROR MESSAGE")
                }
            }
        }
    }
}
