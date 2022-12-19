package com.januscole.githuborganizationsmvvm.data.organizations.repository

import com.januscole.githuborganizationsmvvm.data.github.organizations.datasource.model.toGithubOrganization
import com.januscole.githuborganizationsmvvm.data.github.organizations.repository.OrganizationRepository
import com.januscole.githuborganizationsmvvm.data.github.organizations.repository.OrganizationRepositoryImpl
import com.januscole.githuborganizationsmvvm.data.github.organizations.service.OrganizationSearchService
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData.Companion.VALID_SEARCH_CRITERIA
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData.Companion.VALID_SEARCH_TERM_1
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class GithubOrganizationRepositoryImplTest {

    // Mocks
    private var mockGithubOrganizationService: OrganizationSearchService = Mockito.mock(
        OrganizationSearchService::class.java
    )

    // Class under test
    private var githubOrganizationRepository: OrganizationRepository = OrganizationRepositoryImpl(mockGithubOrganizationService)

    @Test
    fun `Fetching Valid Github Organization Is Successful`() = runTest {

        // Setup
        Mockito.`when`(mockGithubOrganizationService.getGithubOrganization(anyString())).thenReturn(
            MockGithubData().getMockGithubOrganization()
        )

        val expectedResult = Result.success(MockGithubData().getMockGithubOrganization().toGithubOrganization())

        // Execute the test
        val result = githubOrganizationRepository.getGithubOrganization(VALID_SEARCH_CRITERIA)

        // Result
        Assert.assertNotNull(result.getOrNull())
        Assert.assertEquals(expectedResult.getOrNull(), result.getOrNull())
    }

    @Test
    fun `Fetching Valid Github Organization Twice uses Cached Data`() = runTest {

        // Setup
        Mockito.`when`(mockGithubOrganizationService.getGithubOrganization(anyString())).thenReturn(
            MockGithubData().getMockGithubOrganization()
        ).thenThrow()

        val expectedResult = Result.success(MockGithubData().getMockGithubOrganization().toGithubOrganization())

        // Execute the test
        val firstResult = githubOrganizationRepository.getGithubOrganization(VALID_SEARCH_TERM_1)

        // Execute the test
        val secondResult = githubOrganizationRepository.getGithubOrganization(VALID_SEARCH_TERM_1)

        // Result
        Assert.assertNotNull(firstResult)
        Assert.assertNotNull(secondResult)
        Assert.assertEquals(firstResult, secondResult)
        Assert.assertEquals(expectedResult, secondResult)
    }

    @Test
    fun `Fetching Invalid Github Organization Produces Error`() = runTest {

        // Setup
        Mockito.`when`(mockGithubOrganizationService.getGithubOrganization(anyString())).thenThrow()

        // Execute the test
        val result = githubOrganizationRepository.getGithubOrganization(VALID_SEARCH_CRITERIA)

        // Result
        Assert.assertTrue(result.isFailure)
    }
}
