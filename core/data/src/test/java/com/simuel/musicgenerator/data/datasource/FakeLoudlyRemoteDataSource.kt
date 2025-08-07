package com.simuel.musicgenerator.data.datasource

import com.simuel.musicgenerator.data.model.AccountLimitInfoDto
import com.simuel.musicgenerator.data.model.RandomPromptDataDto
import com.simuel.musicgenerator.data.model.SongDto
import com.simuel.musicgenerator.data.model.SongGenerationDto

class FakeLoudlyRemoteDataSource : LoudlyRemoteDataSource {

    var shouldThrowError = false
    var errorCode = 500
    var errorMessage = "Test error"
    private var multipleLimits = false

    override suspend fun getAccountLimits(): List<AccountLimitInfoDto> {
        if (shouldThrowError) {
            throw RuntimeException("$errorCode: $errorMessage")
        }

        return if (multipleLimits) {
            listOf(
                AccountLimitInfoDto(
                    requestType = "song_generation",
                    limit = 100,
                    used = 25,
                    left = 75,
                    dateFrom = "2023-01-01",
                    dateTo = "2023-01-31"
                ),
                AccountLimitInfoDto(
                    requestType = "prompt_generation",
                    limit = 500,
                    used = 150,
                    left = 350,
                    dateFrom = "2023-01-01",
                    dateTo = "2023-01-31"
                )
            )
        } else {
            listOf(
                AccountLimitInfoDto(
                    requestType = "song_generation",
                    limit = 100,
                    used = 25,
                    left = 75,
                    dateFrom = "2023-01-01",
                    dateTo = "2023-01-31"
                )
            )
        }
    }

    override suspend fun generateSong(request: SongGenerationDto): SongDto {
        if (shouldThrowError) {
            throw RuntimeException("$errorCode: $errorMessage")
        }

        return SongDto(
            id = "test-song-id",
            title = "Generated Song: ${request.prompt}",
            duration = (request.durationInSeconds ?: 60) * 1000,
            musicFilePath = "https://api.example.com/songs/test-song.mp3",
            waveFormFilePath = "https://api.example.com/songs/test-song.json",
            createdAt = "2023-01-01T00:00:00Z",
            bpm = 120,
            musicKeyId = 1,
            musicKeyName = "C major",
            musicKeyActive = true
        )
    }

    override suspend fun getRandomPrompt(): RandomPromptDataDto {
        if (shouldThrowError) {
            throw RuntimeException("$errorCode: $errorMessage")
        }

        return RandomPromptDataDto(
            prompt = "Create an upbeat electronic dance track with tropical house vibes"
        )
    }

    fun setupMultipleLimits() {
        multipleLimits = true
    }
}
