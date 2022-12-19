package com.januscole.githuborganizationsmvvm.data.github.organizations.service

import com.januscole.githuborganizationsmvvm.data.github.organizations.datasource.GithubOrganizationRetrofitSearch
import com.januscole.githuborganizationsmvvm.data.github.organizations.datasource.model.GithubOrganizationDTO

class OrganizationSearchServiceImpl(
    private val githubOrganizationRetrofitSearch: GithubOrganizationRetrofitSearch
) : OrganizationSearchService {
    override suspend fun getGithubOrganization(organizationSearchId: String): GithubOrganizationDTO {
        return githubOrganizationRetrofitSearch.getGithubOrganization(organizationSearchId)
    }
}
