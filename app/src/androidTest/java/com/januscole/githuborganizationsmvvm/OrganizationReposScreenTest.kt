package com.januscole.githuborganizationsmvvm

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.accessibility.AccessibilityChecks
import com.januscole.githuborganizationsmvvm.components.GithubRepos
import com.januscole.githuborganizationsmvvm.di.DatastoreModule
import com.januscole.githuborganizationsmvvm.di.NetworkModule
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData
import com.januscole.githuborganizationsmvvm.ui.MainActivity
import com.januscole.githuborganizationsmvvm.ui.theme.GithubOrganizationsMVVMTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatastoreModule::class, NetworkModule::class)
class OrganizationReposScreenTest {

    init {
        AccessibilityChecks.enable().setRunChecksFromRootView(true)
    }

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule(MainActivity::class.java)

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            val navController = rememberNavController()
            GithubOrganizationsMVVMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GithubRepos(navController = navController, organizationId = MockGithubData.VALID_SEARCH_CRITERIA)
                }
            }
        }
    }

    @Test
    fun back_button_is_displayed() {
        // Make sure the back button appears
        composeRule.onNodeWithTag("REPOSBACKBUTTON").assertIsDisplayed()
    }

    @Test
    fun organization_name_is_displayed() {
        // Make sure the organization name appears
        composeRule.onNodeWithText(MockGithubData.VALID_SEARCH_CRITERIA).assertIsDisplayed()
    }
}
