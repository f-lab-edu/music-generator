package com.simuel.musicgenerator.data.repository

import com.simuel.musicgenerator.data.datasource.FakeLoudlyRemoteDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class AccountRepositoryImplTest {

    private lateinit var fakeRemoteDataSource: FakeLoudlyRemoteDataSource
    private lateinit var repository: AccountRepositoryImpl

    @Before
    fun setup() {
        fakeRemoteDataSource = FakeLoudlyRemoteDataSource()
        repository = AccountRepositoryImpl(fakeRemoteDataSource)
    }

    @Test
    fun `계정 한도 정보를 조회하면 원격 데이터소스의 데이터를 반환해야 한다`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowError = false

        // When
        val result = repository.getAccountLimits()

        // Then
        assertNotNull(result)
        assertEquals(1, result.size)
        
        val limitInfo = result[0]
        assertEquals("song_generation", limitInfo.requestType)
        assertEquals(100, limitInfo.limit)
        assertEquals(25, limitInfo.used)
        assertEquals(75, limitInfo.left)
        assertEquals("2023-01-01", limitInfo.dateFrom)
        assertEquals("2023-01-31", limitInfo.dateTo)
    }

    @Test
    fun `계정 한도 정보 조회 시 여러 개의 한도 정보를 반환할 수 있어야 한다`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowError = false
        fakeRemoteDataSource.setupMultipleLimits()

        // When
        val result = repository.getAccountLimits()

        // Then
        assertEquals(2, result.size)
        
        // 첫 번째 한도 정보 확인
        assertEquals("song_generation", result[0].requestType)
        assertEquals(100, result[0].limit)
        
        // 두 번째 한도 정보 확인
        assertEquals("prompt_generation", result[1].requestType)
        assertEquals(500, result[1].limit)
    }
}