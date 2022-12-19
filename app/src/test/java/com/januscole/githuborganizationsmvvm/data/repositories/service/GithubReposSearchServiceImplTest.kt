package com.januscole.githuborganizationsmvvm.data.repositories.service

import com.januscole.githuborganizationsmvvm.data.github.repositories.datasource.GithubReposRetrofitSearch
import com.januscole.githuborganizationsmvvm.data.github.repositories.service.RepositoriesSearchService
import com.januscole.githuborganizationsmvvm.data.github.repositories.service.RepositoriesSearchServiceImpl
import com.januscole.githuborganizationsmvvm.fixtures.MockGithubData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class GithubReposSearchServiceImplTest {

    // Mocks
    private var mockRetrofitService: GithubReposRetrofitSearch = Mockito.mock(
        GithubReposRetrofitSearch::class.java
    )

    // Class under test
    private var githubReposSearchService: RepositoriesSearchService = RepositoriesSearchServiceImpl(mockRetrofitService)

    @Test
    fun `Fetching Repos For Valid Github Organization Is Successful`() = runTest {

        // Setup
        Mockito.`when`(mockRetrofitService.getGithubRepos(ArgumentMatchers.anyString())).thenReturn(
            MockGithubData().getMockGithubOrganizationRepos()
        )

        val expectedResult = MockGithubData().getMockGithubOrganizationRepos()

        // Execute the test
        val result = githubReposSearchService.getGithubRepos(MockGithubData.VALID_SEARCH_CRITERIA)

        // Result
        assertEquals(expectedResult.size, result.size)
        expectedResult.forEachIndexed { index, githubOrganizationRepo ->
            assertEquals(githubOrganizationRepo, result[index])
        }
    }
}
