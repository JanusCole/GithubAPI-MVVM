package com.januscole.githuborganizationsmvvm.data.github.organizations.repository

import com.januscole.githuborganizationsmvvm.data.github.models.Organization
import com.januscole.githuborganizationsmvvm.data.github.organizations.datasource.model.toGithubOrganization
import com.januscole.githuborganizationsmvvm.data.github.organizations.service.OrganizationSearchService
import javax.inject.Inject

class OrganizationRepositoryImpl @Inject constructor(
    private val githubOrganizationSearchService: OrganizationSearchService
) : OrganizationRepository {

    private var organizationCache: Organization? = null

    override suspend fun getGithubOrganization(organizationSearchId: String): Result<Organization> {
        if (organizationCache?.login == organizationSearchId) {
            return Result.success(organizationCache!!)
        }
        return try {
            val result = githubOrganizationSearchService.getGithubOrganization("orgs/$organizationSearchId")
            organizationCache = result.toGithubOrganization()
            Result.success(result.toGithubOrganization())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
