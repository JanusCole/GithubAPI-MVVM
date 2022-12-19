package com.januscole.githuborganizationsmvvm.components.common

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.januscole.githuborganizationsmvvm.R

@Composable
fun ErrorMessage(message: String, onClick: () -> Unit) {

    AlertDialog(
        onDismissRequest = { onClick() },
        title = { Text(text = stringResource(id = R.string.error)) },
        text = {
            Text(
                text = when {
                    message.contains(stringResource(id = R.string.HTTP_403)) -> stringResource(id = R.string.api_rate_limit_exceeded)
                    message.contains(stringResource(id = R.string.HTTP_404)) -> stringResource(id = R.string.organization_not_found)
                    else -> message
                }
            )
        },
        confirmButton = {
            Button(onClick = { onClick() }) {
                Text(text = stringResource(id = R.string.ok))
            }
        }
    )
}
