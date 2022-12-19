package com.januscole.githuborganizationsmvvm.data.github.repositories.repository

import com.januscole.githuborganizationsmvvm.data.github.models.OrganizationRepo
import com.januscole.githuborganizationsmvvm.data.github.repositories.datasource.model.toGithubOrganizationRepos
import com.januscole.githuborganizationsmvvm.data.github.repositories.service.RepositoriesSearchService
import javax.inject.Inject

@Suppress("SpellCheckingInspection")
class RepositoriesRepositoryImpl @Inject constructor(
    private val githubReposSearchService: RepositoriesSearchService
) : RepositoriesRepository {

    private var repoCache: Pair<String, List<OrganizationRepo>>? = null

    override suspend fun getGithubRepos(organizationSearchId: String): Result<List<OrganizationRepo>> {
        if (repoCache?.first == organizationSearchId) {
            return Result.success(repoCache!!.second)
        }
        return try {
            val result = githubReposSearchService.getGithubRepos("orgs/$organizationSearchId/repos")
            repoCache = Pair(organizationSearchId, result.toGithubOrganizationRepos())
            Result.success(result.toGithubOrganizationRepos())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
