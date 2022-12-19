package com.januscole.githuborganizationsmvvm.data.github.repositories.service

import com.januscole.githuborganizationsmvvm.data.github.repositories.datasource.GithubReposRetrofitSearch
import com.januscole.githuborganizationsmvvm.data.github.repositories.datasource.model.GithubOrganizationRepoDTO

class RepositoriesSearchServiceImpl(
    private val githubReposRetrofitSearch: GithubReposRetrofitSearch
) : RepositoriesSearchService {
    override suspend fun getGithubRepos(organizationSearchId: String): List<GithubOrganizationRepoDTO> {
        return githubReposRetrofitSearch.getGithubRepos(organizationSearchId)
    }
}
