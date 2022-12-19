package com.januscole.githuborganizationsmvvm.data.github.repositories.service

import com.januscole.githuborganizationsmvvm.data.github.repositories.datasource.model.GithubOrganizationRepoDTO

interface RepositoriesSearchService {
    suspend fun getGithubRepos(organizationSearchId: String): List<GithubOrganizationRepoDTO>
}
