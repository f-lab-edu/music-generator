package com.simuel.musicgenerator.core.network.api

import com.simuel.musicgenerator.core.network.exception.ApiException
import com.simuel.musicgenerator.core.network.model.AccountLimitResponse
import com.simuel.musicgenerator.core.network.model.GenerateSongRequest
import com.simuel.musicgenerator.core.network.model.MusicKey
import com.simuel.musicgenerator.core.network.model.RandomPromptResponse
import com.simuel.musicgenerator.core.network.model.SongResponse

internal class FakeLoudlyApiService : LoudlyApiService {
    
    var shouldThrowError = false
    var errorCode = 500
    var errorMessage = "Internal Server Error"
    
    override suspend fun getAccountLimits(): List<AccountLimitResponse> {
        if (shouldThrowError) {
            throw ApiException(
                code = errorCode,
                error = "account_limit_error",
                errorDescription = errorMessage
            )
        }
        
        return listOf(
            AccountLimitResponse(
                requestType = "song_generation",
                limit = 100,
                used = 25,
                left = 75,
                dateFrom = "2024-01-01T00:00:00Z",
                dateTo = "2024-12-31T23:59:59Z"
            )
        )
    }
    
    override suspend fun generateSong(request: GenerateSongRequest): SongResponse {
        if (shouldThrowError) {
            throw ApiException(
                code = errorCode,
                error = "generation_failed",
                errorDescription = errorMessage,
                payload = "debug_info"
            )
        }
        
        return SongResponse(
            id = "test-song-id",
            title = "Generated Song: ${request.prompt}",
            duration = request.duration?.times(1000) ?: 60000,
            musicFilePath = "https://api.example.com/songs/test-song.mp3",
            waveFormFilePath = "https://api.example.com/waveforms/test-song.json",
            createdAt = "2024-01-15T10:30:00Z",
            bpm = 120,
            key = MusicKey(
                id = 1,
                name = "C major",
                active = true
            )
        )
    }
    
    override suspend fun getRandomPrompt(): RandomPromptResponse {
        if (shouldThrowError) {
            throw ApiException(
                code = errorCode,
                error = "random_prompt_error",
                errorDescription = errorMessage
            )
        }
        
        return RandomPromptResponse(
            prompt = "Create an upbeat electronic dance track with tropical house vibes"
        )
    }
}
