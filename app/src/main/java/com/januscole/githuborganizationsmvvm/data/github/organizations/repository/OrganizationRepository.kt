package com.januscole.githuborganizationsmvvm.data.github.organizations.repository

import com.januscole.githuborganizationsmvvm.data.github.models.Organization

interface OrganizationRepository {
    suspend fun getGithubOrganization(organizationSearchId: String): Result<Organization>
}
