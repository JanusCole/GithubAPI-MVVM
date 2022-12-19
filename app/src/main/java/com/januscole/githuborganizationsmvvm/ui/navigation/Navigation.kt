package com.januscole.githuborganizationsmvvm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.januscole.githuborganizationsmvvm.components.GithubOrganizationSearch
import com.januscole.githuborganizationsmvvm.components.GithubRepos

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavigationScreen.OrganizationSearch.route) {
        composable(route = NavigationScreen.OrganizationSearch.route) {
            GithubOrganizationSearch(navController)
        }
        composable(
            route = "${NavigationScreen.ReposDisplay.route}/{organizationId}",
            arguments = listOf(navArgument("organizationId") { type = NavType.StringType })
        ) { backStackEntry ->
            GithubRepos(navController = navController, organizationId = backStackEntry.arguments?.getString("organizationId")!!)
        }
    }
}
