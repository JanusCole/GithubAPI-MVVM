package com.januscole.githuborganizationsmvvm.data.github.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrganizationRepo(
    val description: String?,
    val html_url: String,
    val name: String,
    val stargazers_count: Int
) : Parcelable
