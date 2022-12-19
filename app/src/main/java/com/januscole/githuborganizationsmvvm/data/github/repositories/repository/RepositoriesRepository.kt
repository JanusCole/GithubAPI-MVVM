package com.januscole.githuborganizationsmvvm.data.github.repositories.repository

import com.januscole.githuborganizationsmvvm.data.github.models.OrganizationRepo

interface RepositoriesRepository {
    suspend fun getGithubRepos(organizationSearchId: String): Result<List<OrganizationRepo>>
}
