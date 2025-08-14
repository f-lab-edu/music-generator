package com.simuel.musicgenerator.domain.usecase.account

import com.simuel.musicgenerator.domain.model.AccountLimitInfo
import com.simuel.musicgenerator.domain.repository.FakeAccountRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAccountLimitsUseCaseTest {
    
    private lateinit var accountRepository: FakeAccountRepository
    private lateinit var useCase: GetAccountLimitsUseCase
    
    @Before
    fun setup() {
        accountRepository = FakeAccountRepository()
        useCase = GetAccountLimitsUseCase(accountRepository)
    }
    
    @Test
    fun `계정 한도 조회 성공 - 단일 한도`() = runTest {
        // Given
        val expectedLimits = listOf(
            AccountLimitInfo(
                requestType = "generation",
                limit = 10,
                used = 3,
                left = 7,
                dateFrom = "2024-01-01",
                dateTo = "2024-01-31"
            )
        )
        accountRepository.setLimits(expectedLimits)
        
        // When
        val result = useCase()
        
        // Then
        assertEquals(1, result.size)
        assertEquals("generation", result[0].requestType)
        assertEquals(10, result[0].limit)
        assertEquals(3, result[0].used)
        assertEquals(7, result[0].left)
    }
    
    @Test
    fun `계정 한도 조회 성공 - 여러 종류의 한도`() = runTest {
        // Given
        val expectedLimits = listOf(
            AccountLimitInfo(
                requestType = "generation",
                limit = 10,
                used = 5,
                left = 5,
                dateFrom = "2024-01-01",
                dateTo = "2024-01-31"
            ),
            AccountLimitInfo(
                requestType = "download",
                limit = 50,
                used = 20,
                left = 30,
                dateFrom = "2024-01-01",
                dateTo = "2024-01-31"
            ),
            AccountLimitInfo(
                requestType = "api_calls",
                limit = 1000,
                used = 450,
                left = 550,
                dateFrom = "2024-01-01",
                dateTo = "2024-01-31"
            )
        )
        accountRepository.setLimits(expectedLimits)
        
        // When
        val result = useCase()
        
        // Then
        assertEquals(3, result.size)
        
        val generationLimit = result.find { it.requestType == "generation" }
        assertEquals(10, generationLimit?.limit)
        assertEquals(5, generationLimit?.left)
        
        val downloadLimit = result.find { it.requestType == "download" }
        assertEquals(50, downloadLimit?.limit)
        assertEquals(30, downloadLimit?.left)
        
        val apiLimit = result.find { it.requestType == "api_calls" }
        assertEquals(1000, apiLimit?.limit)
        assertEquals(550, apiLimit?.left)
    }
    
    @Test
    fun `계정 한도 조회 - 한도 없음`() = runTest {
        // Given
        accountRepository.setLimits(emptyList())
        
        // When
        val result = useCase()
        
        // Then
        assertTrue(result.isEmpty())
    }
    
    @Test
    fun `계정 한도 조회 - 한도 모두 소진`() = runTest {
        // Given
        val exhaustedLimits = listOf(
            AccountLimitInfo(
                requestType = "generation",
                limit = 10,
                used = 10,
                left = 0,
                dateFrom = "2024-01-01",
                dateTo = "2024-01-31"
            )
        )
        accountRepository.setLimits(exhaustedLimits)
        
        // When
        val result = useCase()
        
        // Then
        assertEquals(1, result.size)
        assertEquals(0, result[0].left)
        assertEquals(10, result[0].used)
        assertEquals(10, result[0].limit)
    }
    
    @Test
    fun `계정 한도 조회 - 날짜 정보 확인`() = runTest {
        // Given
        val limitsWithDates = listOf(
            AccountLimitInfo(
                requestType = "generation",
                limit = 5,
                used = 2,
                left = 3,
                dateFrom = "2024-01-15",
                dateTo = "2024-02-15"
            )
        )
        accountRepository.setLimits(limitsWithDates)
        
        // When
        val result = useCase()
        
        // Then
        assertEquals("2024-01-15", result[0].dateFrom)
        assertEquals("2024-02-15", result[0].dateTo)
    }
}