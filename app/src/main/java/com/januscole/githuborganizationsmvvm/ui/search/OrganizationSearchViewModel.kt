package com.januscole.githuborganizationsmvvm.ui.search

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.januscole.githuborganizationsmvvm.data.github.models.Organization
import com.januscole.githuborganizationsmvvm.data.github.organizations.repository.OrganizationRepository
import com.januscole.githuborganizationsmvvm.data.searches.repository.SearchCriteriaDatastoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

const val SAVED_SEARCHES = "SAVED_SEARCHES"
const val UI_STATE = "UI_STATE"

@HiltViewModel
class OrganizationSearchViewModel @Inject constructor(
    private val githubOrganizationRepository: OrganizationRepository,
    private val savedSearchesRepository: SearchCriteriaDatastoreRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @Parcelize
    data class GithubOrganizationUiState(
        val organization: Organization? = null,
        val isLoading: Boolean = false,
        val exception: Throwable? = null
    ) : Parcelable

    val savedSearches = savedStateHandle.getStateFlow<List<String>>(SAVED_SEARCHES, listOf())
    val uiState = savedStateHandle.getStateFlow(UI_STATE, GithubOrganizationUiState())

    init {
        viewModelScope.launch {
            savedSearchesRepository.savedSearches().collect {
                savedStateHandle[SAVED_SEARCHES] = it.toList()
            }
        }
    }

    fun fetchOrganization(githubOrganizationId: String) {
        viewModelScope.launch {
            savedStateHandle[UI_STATE] = uiState.value.copy(isLoading = true)
            val result = githubOrganizationRepository.getGithubOrganization(githubOrganizationId)
            when {
                result.isSuccess -> {
                    savedStateHandle[UI_STATE] =
                        uiState.value.copy(
                            organization = result.getOrNull(),
                            isLoading = false,
                            exception = null
                        )
                    savedSearchesRepository.addSearch(githubOrganizationId)
                }
                result.isFailure -> {
                    savedStateHandle[UI_STATE] = uiState.value.copy(isLoading = false, exception = result.exceptionOrNull())
                }
            }
        }
    }

    // This is my least favorite solution to the problem of one time consumable events in
    // JetPack Compose. However, this is what Google currently recommends and so resistance is futile
    // https://developer.android.com/topic/architecture/ui-layer/events#compose_2
    fun consumeSearchEvent() {
        savedStateHandle[UI_STATE] = uiState.value.copy(organization = null, isLoading = false, exception = null)
    }
}
