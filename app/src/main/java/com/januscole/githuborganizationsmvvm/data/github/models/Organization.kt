package com.januscole.githuborganizationsmvvm.data.github.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Organization(
    val avatar_url: String,
    val login: String
) : Parcelable
