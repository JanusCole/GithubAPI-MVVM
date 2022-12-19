package com.januscole.githuborganizationsmvvm.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.januscole.githuborganizationsmvvm.R
import com.januscole.githuborganizationsmvvm.data.github.models.OrganizationRepo
import com.januscole.githuborganizationsmvvm.ui.theme.Purple200
import com.januscole.githuborganizationsmvvm.ui.theme.Purple500

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RepoCard(repo: OrganizationRepo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.repo_card_padding)),
        backgroundColor = Purple200,
        elevation = dimensionResource(id = R.dimen.repo_card_padding),
        border = BorderStroke(dimensionResource(id = R.dimen.repo_card_border_stroke), Color.Black),
        contentColor = Color.DarkGray,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.repo_card_padding)),
        onClick = {
            onClick()
        }
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.repo_card_padding))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                ) {
                    Text(
                        text = repo.name,
                        style = MaterialTheme.typography.h6,
                        color = Color.White
                    )
                }
                Column {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_star_outline_24),
                            contentDescription = stringResource(id = R.string.repo_number_of_stars_hint),
                            tint = Purple500
                        )
                        Text(
                            text = repo.stargazers_count.toString(),
                            style = MaterialTheme.typography.h6,
                            color = Color.White
                        )
                    }
                }
            }
            Row {
                repo.description?.let {
                    Text(
                        text = repo.description,
                        style = MaterialTheme.typography.h6,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.Start)
                    )
                }
            }
        }
    }
}
