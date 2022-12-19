package com.januscole.githuborganizationsmvvm.components

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.januscole.githuborganizationsmvvm.R
import com.januscole.githuborganizationsmvvm.components.common.ErrorMessage
import com.januscole.githuborganizationsmvvm.ui.repos.OrganizationRepositoriesViewModel

private const val REPO_LIMIT = 3

@Composable
fun GithubRepos(
    navController: NavController,
    organizationId: String,
    githubReposViewModel: OrganizationRepositoriesViewModel = hiltViewModel()
) {

    val uiState by githubReposViewModel.uiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        githubReposViewModel.fetchRepos(organizationId)
    }

    Column {
        Row {
            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .testTag(stringResource(id = R.string.REPOSBACKBUTTON_TEST_TAG))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back_ios_24),
                    contentDescription = stringResource(id = R.string.back_button_accessibility_hint),
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .align(
                            Alignment.CenterVertically
                        )
                )
            }
        }
        Row {
            uiState.organization?.let { organization ->
                AsyncImage(
                    model = organization.avatar_url,
                    contentDescription = organization.login,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.organization_avatar_size))
                        .wrapContentWidth(Alignment.Start)
                        .padding(dimensionResource(id = R.dimen.standard_padding), 0.dp)
                )
                Text(
                    text = organization.login.capitalize(Locale.current),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        uiState.repos?.let { repositories ->
            if (repositories.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_repos_found),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(dimensionResource(id = R.dimen.standard_padding))
                )
            } else {
                LazyColumn {
                    items(
                        repositories.sortedByDescending { it.stargazers_count }
                            .take(REPO_LIMIT)
                    ) { repo ->
                        RepoCard(repo) {
                            CustomTabsIntent.Builder().build().run {
                                launchUrl(context, Uri.parse(repo.html_url))
                            }
                        }
                    }
                }
            }
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.loading_indicator_padding))
            )
        }

        uiState.exception?.let {
            ErrorMessage(message = it.message ?: stringResource(id = R.string.UNKNOWN_ERROR)) { githubReposViewModel.consumeSearchEvent() }
        }
    }
}
