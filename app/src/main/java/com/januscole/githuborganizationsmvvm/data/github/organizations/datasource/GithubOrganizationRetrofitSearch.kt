package com.januscole.githuborganizationsmvvm.data.github.organizations.datasource

import com.januscole.githuborganizationsmvvm.data.github.organizations.datasource.model.GithubOrganizationDTO
import retrofit2.http.GET
import retrofit2.http.Url

interface GithubOrganizationRetrofitSearch {
    @GET
    suspend fun getGithubOrganization(@Url organizationSearchUrl: String): GithubOrganizationDTO
}
