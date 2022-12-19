package com.januscole.githuborganizationsmvvm.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.januscole.githuborganizationsmvvm.R
import com.januscole.githuborganizationsmvvm.components.common.ErrorMessage
import com.januscole.githuborganizationsmvvm.ui.navigation.NavigationScreen
import com.januscole.githuborganizationsmvvm.ui.search.OrganizationSearchViewModel

@Composable
fun GithubOrganizationSearch(
    navController: NavController,
    githubOrganizationViewModel: OrganizationSearchViewModel = hiltViewModel()
) {

    val uiState by githubOrganizationViewModel.uiState.collectAsState()
    val savedSearches by githubOrganizationViewModel.savedSearches.collectAsState()

    var searchCriteria by remember { mutableStateOf("") }
    var expandAutoSuggestions by remember { mutableStateOf(false) }

    uiState.organization?.let {
        // This is my least favorite solution to the problem of one time consumable events in
        // JetPack Compose. However, this is what Google currently recommends and so resistance is futile
        // https://developer.android.com/topic/architecture/ui-layer/events#compose_2
        githubOrganizationViewModel.consumeSearchEvent()
        navController.navigate(NavigationScreen.ReposDisplay.route + "/" + it.login)
    }

    Column {
        Image(
            painterResource(id = R.drawable.github_mark),
            contentDescription = stringResource(id = R.string.search_github_organizations_hint),
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(dimensionResource(id = R.dimen.standard_padding))
                .align(
                    Alignment.CenterHorizontally
                )
                .background(Color.White)
        )
        Row(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.standard_padding))
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.organization_avatar_size))
            ) {
                OutlinedTextField(
                    value = searchCriteria,
                    onValueChange = {
                        searchCriteria = it
                        expandAutoSuggestions = true
                    },
                    maxLines = 1,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.standard_padding))
                        .fillMaxWidth()
                        .onFocusChanged {
                            expandAutoSuggestions = it.hasFocus
                        }
                        .testTag(stringResource(id = R.string.SEARCHTEXT_TEST_TAG)),
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.search_text_accessibility_hint),
                            tint = Color.Black
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White
                    )
                )
                // Inspired by some autocomplete examples from the internet which mostly didn't actually work
                val autocompleteSuggestions = savedSearches.filter { it.contains(searchCriteria, ignoreCase = true) }
                if (autocompleteSuggestions.isNotEmpty() && expandAutoSuggestions) {
                    DropdownMenu(
                        expanded = expandAutoSuggestions,
                        properties = PopupProperties(focusable = false),
                        onDismissRequest = { expandAutoSuggestions = false },
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .testTag(stringResource(id = R.string.AUTOCOMPLETE_TEST_TAG))
                    ) {
                        autocompleteSuggestions.forEach { suggestion ->
                            DropdownMenuItem(onClick = {
                                searchCriteria = suggestion
                                expandAutoSuggestions = false
                            }) {
                                Text(text = suggestion)
                            }
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                githubOrganizationViewModel.fetchOrganization(searchCriteria)
            },
            enabled = !uiState.isLoading && searchCriteria != "",
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.search),
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.loading_indicator_padding))
            )
        }

        uiState.exception?.let {
            ErrorMessage(message = it.message ?: stringResource(id = R.string.UNKNOWN_ERROR)) { githubOrganizationViewModel.consumeSearchEvent() }
        }
    }
}
