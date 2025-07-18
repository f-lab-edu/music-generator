package com.simuel.musicgenerator.core.network.datasource

import com.simuel.musicgenerator.core.network.api.FakeLoudlyApiService
import com.simuel.musicgenerator.core.network.exception.ApiException
import com.simuel.musicgenerator.data.datasource.LoudlyRemoteDataSource
import com.simuel.musicgenerator.data.model.SongGenerationDto
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class LoudlyRemoteDataSourceImplTest {
    
    private lateinit var fakeApiService: FakeLoudlyApiService
    private lateinit var dataSource: LoudlyRemoteDataSource
    
    @Before
    fun setup() {
        fakeApiService = FakeLoudlyApiService()
        dataSource = LoudlyRemoteDataSourceImpl(fakeApiService)
    }
    
    @Test
    fun `계정 한도 조회 시 성공적으로 데이터를 반환해야 한다`() = runTest {
        // Given
        fakeApiService.shouldThrowError = false
        
        // When
        val result = dataSource.getAccountLimits()
        
        // Then
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("song_generation", result[0].requestType)
        assertEquals(100, result[0].limit)
        assertEquals(25, result[0].used)
        assertEquals(75, result[0].left)
    }
    
    @Test
    fun `계정 한도 조회 시 API 에러가 발생하면 ApiException을 던져야 한다`() = runTest {
        // Given
        fakeApiService.shouldThrowError = true
        fakeApiService.errorCode = 500
        fakeApiService.errorMessage = "Server Error"
        
        // When & Then
        val exception = assertThrows(ApiException::class.java) {
            runBlocking { dataSource.getAccountLimits() }
        }
        
        assertEquals(500, exception.code)
        assertEquals("account_limit_error", exception.error)
        assertEquals("Server Error", exception.errorDescription)
    }
    
    @Test
    fun `노래 생성 요청 시 성공적으로 데이터를 반환해야 한다`() = runTest {
        // Given
        fakeApiService.shouldThrowError = false
        val request = SongGenerationDto(
            prompt = "Test prompt",
            durationInSeconds = 60,
            isTest = false,
            structureId = null
        )
        
        // When
        val result = dataSource.generateSong(request)
        
        // Then
        assertNotNull(result)
        assertEquals("test-song-id", result.id)
        assertEquals("Generated Song: Test prompt", result.title)
        assertEquals(60000, result.durationInMillis)
        assertEquals("https://api.example.com/songs/test-song.mp3", result.musicFileUrl)
        assertEquals(120, result.bpm)
        assertNotNull(result.key)
        assertEquals("C major", result.key?.name)
    }
    
    @Test
    fun `노래 생성 요청 시 500 에러가 발생하면 ApiException을 던져야 한다`() = runTest {
        // Given
        fakeApiService.shouldThrowError = true
        fakeApiService.errorCode = 500
        fakeApiService.errorMessage = "Failed to generate song"
        val request = SongGenerationDto(prompt = "Test prompt")
        
        // When & Then
        val exception = assertThrows(ApiException::class.java) {
            runBlocking { dataSource.generateSong(request) }
        }
        
        assertEquals(500, exception.code)
        assertEquals("generation_failed", exception.error)
        assertEquals("Failed to generate song", exception.errorDescription)
        assertEquals("debug_info", exception.payload)
    }
    
    @Test
    fun `랜덤 프롬프트 조회 시 성공적으로 데이터를 반환해야 한다`() = runTest {
        // Given
        fakeApiService.shouldThrowError = false
        
        // When
        val result = dataSource.getRandomPrompt()
        
        // Then
        assertNotNull(result)
        assertEquals("Create an upbeat electronic dance track with tropical house vibes", result.prompt)
    }
    
    @Test
    fun `랜덤 프롬프트 조회 시 API 에러가 발생하면 ApiException을 던져야 한다`() = runTest {
        // Given
        fakeApiService.shouldThrowError = true
        fakeApiService.errorCode = 401
        fakeApiService.errorMessage = "Unauthorized"
        
        // When & Then
        val exception = assertThrows(ApiException::class.java) {
            runBlocking { dataSource.getRandomPrompt() }
        }
        
        assertEquals(401, exception.code)
        assertEquals("random_prompt_error", exception.error)
        assertEquals("Unauthorized", exception.errorDescription)
    }
}