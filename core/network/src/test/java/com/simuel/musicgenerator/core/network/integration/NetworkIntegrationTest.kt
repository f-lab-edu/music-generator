package com.simuel.musicgenerator.core.network.integration

import com.simuel.musicgenerator.core.network.exception.ApiException
import com.simuel.musicgenerator.core.network.interceptor.ApiKeyInterceptor
import com.simuel.musicgenerator.core.network.interceptor.ErrorHandlingInterceptor
import com.simuel.musicgenerator.core.network.model.AccountLimitResponse
import com.simuel.musicgenerator.core.network.model.RandomPromptResponse
import com.simuel.musicgenerator.core.network.model.SongResponse
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NetworkIntegrationTest {
    
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }
    
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ErrorHandlingInterceptor(json))
            .build()
    }
    
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
    
    @Test
    fun `계정 한도 조회 API가 200으로 응답할 때 AccountLimitResponse 리스트를 반환해야 한다`() {
        // Given
        val accountLimitsJson = """
            [
                {
                    "request_type": "song_generation",
                    "limit": 100,
                    "used": 25,
                    "left": 75,
                    "date_from": "2024-01-01T00:00:00Z",
                    "date_to": "2024-12-31T23:59:59Z"
                }
            ]
        """.trimIndent()
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(accountLimitsJson)
                .setHeader("Content-Type", "application/json")
        )
        
        val request = Request.Builder()
            .url("${mockWebServer.url("/api/account/limits")}")
            .build()
        
        // When
        val response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""
        val accountLimits = json.decodeFromString<List<AccountLimitResponse>>(responseBody)
        
        // Then
        assertEquals(200, response.code)
        assertEquals(1, accountLimits.size)
        assertEquals("song_generation", accountLimits[0].requestType)
        assertEquals(100, accountLimits[0].limit)
        assertEquals(25, accountLimits[0].used)
        assertEquals(75, accountLimits[0].left)
    }
    
    @Test
    fun `노래 생성 API가 200으로 응답할 때 SongResponse를 반환해야 한다`() {
        // Given
        val songResponseJson = """
            {
                "id": "550e8400-e29b-41d4-a716-446655440000",
                "title": "Generated Song",
                "duration": 120000,
                "music_file_path": "https://api.example.com/songs/550e8400.mp3",
                "wave_form_file_path": "https://api.example.com/waveforms/550e8400.json",
                "created_at": "2024-01-15T10:30:00Z",
                "bpm": 128,
                "key": {
                    "id": 1,
                    "name": "C major",
                    "active": true
                }
            }
        """.trimIndent()
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(songResponseJson)
                .setHeader("Content-Type", "application/json")
        )
        
        val request = Request.Builder()
            .url("${mockWebServer.url("/api/ai/prompt/songs")}")
            .build()
        
        // When
        val response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""
        val songResponse = json.decodeFromString<SongResponse>(responseBody)
        
        // Then
        assertEquals(200, response.code)
        assertEquals("550e8400-e29b-41d4-a716-446655440000", songResponse.id)
        assertEquals("Generated Song", songResponse.title)
        assertEquals(120000, songResponse.duration)
        assertEquals(128, songResponse.bpm)
    }
    
    @Test
    fun `노래 생성 API가 500으로 응답할 때 ApiException을 던져야 한다`() {
        // Given
        val errorResponseJson = """
            {
                "error": "generation_failed",
                "error_description": "Failed to generate song due to server error",
                "payload": "debug_info_123"
            }
        """.trimIndent()
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody(errorResponseJson)
                .setHeader("Content-Type", "application/json")
        )
        
        val request = Request.Builder()
            .url("${mockWebServer.url("/api/ai/prompt/songs")}")
            .build()
        
        // When & Then
        val exception = assertThrows(ApiException::class.java) {
            okHttpClient.newCall(request).execute()
        }
        
        assertEquals(500, exception.code)
        assertEquals("generation_failed", exception.error)
        assertEquals("Failed to generate song due to server error", exception.errorDescription)
        assertEquals("debug_info_123", exception.payload)
    }
    
    @Test
    fun `랜덤 프롬프트 API가 200으로 응답할 때 RandomPromptResponse를 반환해야 한다`() {
        // Given
        val randomPromptJson = """
            {
                "prompt": "Create an upbeat electronic dance track with tropical house vibes"
            }
        """.trimIndent()
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(randomPromptJson)
                .setHeader("Content-Type", "application/json")
        )
        
        val request = Request.Builder()
            .url("${mockWebServer.url("/api/ai/prompt/random")}")
            .build()
        
        // When
        val response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""
        val randomPrompt = json.decodeFromString<RandomPromptResponse>(responseBody)
        
        // Then
        assertEquals(200, response.code)
        assertEquals("Create an upbeat electronic dance track with tropical house vibes", randomPrompt.prompt)
    }
    
    @Test
    fun `ApiKeyInterceptor가 API-KEY 헤더를 추가해야 한다`() {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{}")
        )
        
        val clientWithApiKey = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .build()
        
        val request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .build()
        
        // When
        clientWithApiKey.newCall(request).execute()
        
        // Then
        val recordedRequest = mockWebServer.takeRequest()
        assertNotNull(recordedRequest.getHeader("API-KEY"))
        // BuildConfig.LOUDLY_API_KEY가 테스트 환경에서는 빈 문자열일 수 있음
        // 헤더가 추가되었는지만 확인
    }
    
}