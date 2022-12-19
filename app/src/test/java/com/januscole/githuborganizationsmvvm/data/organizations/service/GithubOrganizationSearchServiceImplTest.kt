package com.januscole.githuborganizationsmvvm.data.organizations.service

import com.januscole.githuborganizationsmvvm.data.github.organizations.datasource.GithubOrganizationRetrofitSearch
import com.januscole.githuborganizationsmvvm.data.github.organizations.service.OrganizationSearchService
import com.januscole.githuborganizationsmvvm.data.github.organizations.service.OrganizationSearchServiceImpl
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData.Companion.VALID_SEARCH_CRITERIA
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
class GithubOrganizationSearchServiceImplTest {

    // Mocks
    private var mockRetrofitService: GithubOrganizationRetrofitSearch = Mockito.mock(
        GithubOrganizationRetrofitSearch::class.java
    )

    // Class under test
    private var githubOrganizationSearchService: OrganizationSearchService = OrganizationSearchServiceImpl(mockRetrofitService)

    @Test
    fun `Fetching Valid Github Organization Is Successful`() = runTest {

        // Setup
        Mockito.`when`(mockRetrofitService.getGithubOrganization(anyString())).thenReturn(MockGithubData().getMockGithubOrganization())

        val expectedResult = MockGithubData().getMockGithubOrganization()

        // Execute the test
        val result = githubOrganizationSearchService.getGithubOrganization(VALID_SEARCH_CRITERIA)

        // Result
        Assert.assertEquals(expectedResult, result)
    }
}
