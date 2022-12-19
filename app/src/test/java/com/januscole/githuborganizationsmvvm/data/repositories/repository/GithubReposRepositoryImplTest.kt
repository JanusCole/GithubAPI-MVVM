package com.januscole.githuborganizationsmvvm.data.repositories.repository

import com.januscole.githuborganizationsmvvm.data.github.repositories.datasource.model.toGithubOrganizationRepos
import com.januscole.githuborganizationsmvvm.data.github.repositories.repository.RepositoriesRepository
import com.januscole.githuborganizationsmvvm.data.github.repositories.repository.RepositoriesRepositoryImpl
import com.januscole.githuborganizationsmvvm.data.github.repositories.service.RepositoriesSearchService
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
class GithubReposRepositoryImplTest {

    // Mocks
    private var mockRepositoriesService: RepositoriesSearchService = Mockito.mock(
        RepositoriesSearchService::class.java
    )

    // Class under test
    private var githubReposRepository: RepositoriesRepository = RepositoriesRepositoryImpl(mockRepositoriesService)

    @Test
    fun `Fetching Repos For Valid Github Organization Is Successful`() = runTest {

        // Setup
        Mockito.`when`(mockRepositoriesService.getGithubRepos(ArgumentMatchers.anyString())).thenReturn(
            MockGithubData().getMockGithubOrganizationRepos()
        )
        githubReposRepository = RepositoriesRepositoryImpl(mockRepositoriesService)

        val expectedResult = Result.success(MockGithubData().getMockGithubOrganizationRepos().toGithubOrganizationRepos())

        // Execute the test
        val result = githubReposRepository.getGithubRepos(MockGithubData.VALID_SEARCH_CRITERIA)

        // Result
        assertNotNull(result)
        assertEquals(expectedResult.getOrNull()?.size, result.getOrNull()?.size)
        expectedResult.getOrNull()!!.forEachIndexed { index, githubOrganizationRepo ->
            assertEquals(githubOrganizationRepo, result.getOrNull()!![index])
        }
    }

    @Test
    fun `Fetching Repos For Invalid Github Organization Produces Error`() = runTest {

        // Setup
        Mockito.`when`(mockRepositoriesService.getGithubRepos(ArgumentMatchers.anyString())).thenThrow()

        // Execute the test
        val result = githubReposRepository.getGithubRepos(MockGithubData.VALID_SEARCH_CRITERIA)

        // Result
        assertTrue(result.isFailure)
    }

    @Test
    fun `Fetching A Valid Github Repo Twice uses Cached Data`() = runTest {

        // Setup
        Mockito.`when`(mockRepositoriesService.getGithubRepos(ArgumentMatchers.anyString())).thenReturn(
            MockGithubData().getMockGithubOrganizationRepos()
        ).thenThrow()

        val expectedResult = Result.success(MockGithubData().getMockGithubOrganizationRepos().toGithubOrganizationRepos())

        // Execute the test
        val firstResult = githubReposRepository.getGithubRepos(MockGithubData.VALID_SEARCH_TERM_1)

        // Execute the test
        val secondResult = githubReposRepository.getGithubRepos(MockGithubData.VALID_SEARCH_TERM_1)

        // Result
        assertNotNull(firstResult)
        assertNotNull(secondResult)
        assertEquals(firstResult, secondResult)
        assertEquals(expectedResult, secondResult)
    }
}
