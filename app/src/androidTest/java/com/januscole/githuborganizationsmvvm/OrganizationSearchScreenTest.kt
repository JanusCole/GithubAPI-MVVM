package com.januscole.githuborganizationsmvvm

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.accessibility.AccessibilityChecks
import com.januscole.githuborganizationsmvvm.di.DatastoreModule
import com.januscole.githuborganizationsmvvm.di.NetworkModule
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData
import com.januscole.githuborganizationsmvvm.ui.MainActivity
import com.januscole.githuborganizationsmvvm.ui.navigation.Navigation
import com.januscole.githuborganizationsmvvm.ui.theme.GithubOrganizationsMVVMTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatastoreModule::class, NetworkModule::class)
class OrganizationSearchScreenTest {

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
            GithubOrganizationsMVVMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navigation()
                }
            }
        }
    }

    @Test
    fun search_button_is_displayed() {
        // Make sure the Search button appears
        composeRule.onNodeWithText("Search").assertIsDisplayed()
    }

    @Test
    fun search_button_is_disabled() {
        // Make sure the Search button is disabled before a search term is entered
        composeRule.onNodeWithText("Search").assertIsNotEnabled()
    }

    @Test
    fun search_button_is_enabled() {
        // Make sure the Search button is enabled when a search term is entered
        composeRule.onNodeWithTag("SEARCHTEXT").performTextInput(MockGithubData.VALID_SEARCH_CRITERIA)
        composeRule.onNodeWithText("Search").assertIsEnabled()
    }

    @Test
    fun search_valid_criteria() {
        // A successful search navigates to the repos screen
        composeRule.onNodeWithTag("SEARCHTEXT").performTextInput(MockGithubData.VALID_SEARCH_CRITERIA)
        composeRule.onNodeWithText("Search").performClick()
        composeRule.waitUntil {
            composeRule
                .onAllNodesWithTag("REPOSBACKBUTTON")
                .fetchSemanticsNodes().size == 1
        }
        composeRule.onNodeWithTag("REPOSBACKBUTTON").assertIsDisplayed()
    }

    @Test
    fun search_invalid_criteria() {
        // An unsuccessful search displays an error screen
        composeRule.onNodeWithTag("SEARCHTEXT").performTextInput("X")
        composeRule.onNodeWithText("Search").performClick()
        composeRule.waitUntil {
            composeRule
                .onAllNodesWithText("Error")
                .fetchSemanticsNodes().size == 1
        }
        composeRule.onNodeWithText("Error").assertIsDisplayed()
    }
}
