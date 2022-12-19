package com.januscole.githuborganizationsmvvm.ui.repos

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.januscole.githuborganizationsmvvm.data.github.models.Organization
import com.januscole.githuborganizationsmvvm.data.github.models.OrganizationRepo
import com.januscole.githuborganizationsmvvm.data.github.organizations.repository.OrganizationRepository
import com.januscole.githuborganizationsmvvm.data.github.repositories.repository.RepositoriesRepository
import com.januscole.githuborganizationsmvvm.ui.search.UI_STATE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@HiltViewModel
class OrganizationRepositoriesViewModel @Inject constructor(
    private val githubOrganizationRepository: OrganizationRepository,
    private val githubReposRepository: RepositoriesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @Parcelize
    data class GithubReposUiState(
        val organization: Organization? = null,
        val repos: List<OrganizationRepo>? = null,
        val isLoading: Boolean = false,
        val exception: Throwable? = null
    ) : Parcelable

    val uiState = savedStateHandle.getStateFlow(
        UI_STATE,
        GithubReposUiState()
    )

    fun fetchRepos(githubOrganizationId: String) {
        viewModelScope.launch {
            savedStateHandle[UI_STATE] = uiState.value.copy(isLoading = true)
            val organizationResult = githubOrganizationRepository.getGithubOrganization(githubOrganizationId)
            when {
                organizationResult.isSuccess -> {
                    savedStateHandle[UI_STATE] = uiState.value.copy(organization = organizationResult.getOrNull())
                }
                organizationResult.isFailure -> {
                    savedStateHandle[UI_STATE] = uiState.value.copy(isLoading = false, exception = organizationResult.exceptionOrNull())
                }
            }
            if (uiState.value.exception == null) {
                val reposResult = githubReposRepository.getGithubRepos(githubOrganizationId)
                when {
                    reposResult.isSuccess -> {
                        savedStateHandle[UI_STATE] = uiState.value.copy(
                            repos = reposResult.getOrNull(),
                            isLoading = false
                        )
                    }
                    reposResult.isFailure -> {
                        savedStateHandle[UI_STATE] = uiState.value.copy(
                            isLoading = false,
                            exception = reposResult.exceptionOrNull()
                        )
                    }
                }
            }
        }
    }
    // This is my least favorite solution to the problem of one time consumable events in
    // JetPack Compose. However, this is what Google currently recommends and so resistance is futile
    // https://developer.android.com/topic/architecture/ui-layer/events#compose_2
    fun consumeSearchEvent() {
        savedStateHandle[UI_STATE] = uiState.value.copy(organization = null, repos = null, isLoading = false, exception = null)
    }
}
