package com.januscole.githuborganizationsmvvm.data.github.repositories.datasource

import com.januscole.githuborganizationsmvvm.data.github.repositories.datasource.model.GithubOrganizationRepoDTO
import retrofit2.http.GET
import retrofit2.http.Url

interface GithubReposRetrofitSearch {
    @GET
    suspend fun getGithubRepos(@Url organizationSearchUrl: String): List<GithubOrganizationRepoDTO>
}
