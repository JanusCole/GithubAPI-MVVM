package com.januscole.githuborganizationsmvvm.data.github.organizations.service

import com.januscole.githuborganizationsmvvm.data.github.organizations.datasource.model.GithubOrganizationDTO

interface OrganizationSearchService {
    suspend fun getGithubOrganization(organizationSearchId: String): GithubOrganizationDTO
}
