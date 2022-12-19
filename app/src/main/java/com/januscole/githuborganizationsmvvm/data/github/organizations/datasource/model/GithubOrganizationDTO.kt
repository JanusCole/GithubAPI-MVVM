package com.januscole.githuborganizationsmvvm.data.github.organizations.datasource.model

import com.januscole.githuborganizationsmvvm.data.github.models.Organization

data class GithubOrganizationDTO(
    val avatar_url: String,
    val blog: String,
    val company: Any,
    val created_at: String,
    val description: Any,
    val email: Any,
    val events_url: String,
    val followers: Int,
    val following: Int,
    val has_organization_projects: Boolean,
    val has_repository_projects: Boolean,
    val hooks_url: String,
    val html_url: String,
    val id: Int,
    val is_verified: Boolean,
    val issues_url: String,
    val location: Any,
    val login: String,
    val members_url: String,
    val name: String,
    val node_id: String,
    val public_gists: Int,
    val public_members_url: String,
    val public_repos: Int,
    val repos_url: String,
    val twitter_username: Any,
    val type: String,
    val updated_at: String,
    val url: String
)

fun GithubOrganizationDTO.toGithubOrganization(): Organization {
    return Organization(
        avatar_url = this.avatar_url,
        login = this.login
    )
}
