package com.simuel.musicgenerator.data.repository

import com.simuel.musicgenerator.data.datasource.FakeLoudlyRemoteDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class PromptRepositoryImplTest {

    private lateinit var fakeRemoteDataSource: FakeLoudlyRemoteDataSource
    private lateinit var repository: PromptRepositoryImpl

    @Before
    fun setup() {
        fakeRemoteDataSource = FakeLoudlyRemoteDataSource()
        repository = PromptRepositoryImpl(fakeRemoteDataSource)
    }

    @Test
    fun `랜덤 프롬프트를 조회하면 원격 데이터소스의 프롬프트를 반환해야 한다`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowError = false

        // When
        val result = repository.getRandomPrompt()

        // Then
        assertNotNull(result)
        assertEquals("Create an upbeat electronic dance track with tropical house vibes", result.prompt)
    }

    @Test
    fun `여러 번 호출해도 프롬프트를 정상적으로 반환해야 한다`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowError = false

        // When
        val result1 = repository.getRandomPrompt()
        val result2 = repository.getRandomPrompt()

        // Then
        assertNotNull(result1)
        assertNotNull(result2)
        assertEquals(result1.prompt, result2.prompt) // Fake에서는 동일한 프롬프트 반환
    }
}