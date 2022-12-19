package com.januscole.githuborganizationsmvvm.ui.navigation

sealed class NavigationScreen(val route: String) {
    object OrganizationSearch : NavigationScreen("organization")
    object ReposDisplay : NavigationScreen("repos")
}
