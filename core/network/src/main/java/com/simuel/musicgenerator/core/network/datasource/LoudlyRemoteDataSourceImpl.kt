package com.simuel.musicgenerator.core.network.datasource

import com.simuel.musicgenerator.core.network.api.LoudlyApiService
import com.simuel.musicgenerator.core.network.model.GenerateSongRequest as NetworkGenerateSongRequest
import com.simuel.musicgenerator.data.datasource.LoudlyRemoteDataSource
import com.simuel.musicgenerator.data.model.AccountLimitInfoDto
import com.simuel.musicgenerator.data.model.SongGenerationDto
import com.simuel.musicgenerator.data.model.RandomPromptDataDto
import com.simuel.musicgenerator.data.model.SongDto
import com.simuel.musicgenerator.data.model.MusicKeyDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

internal class LoudlyRemoteDataSourceImpl @Inject constructor(
    private val apiService: LoudlyApiService
) : LoudlyRemoteDataSource {

    override suspend fun getAccountLimits(): List<AccountLimitInfoDto> {
        val response = apiService.getAccountLimits()
        return response.map { accountLimitResponse ->
            AccountLimitInfoDto(
                requestType = accountLimitResponse.requestType,
                limit = accountLimitResponse.limit,
                used = accountLimitResponse.used,
                left = accountLimitResponse.left,
                dateFrom = accountLimitResponse.dateFrom,
                dateTo = accountLimitResponse.dateTo
            )
        }
    }

    override suspend fun generateSong(request: SongGenerationDto): SongDto {
        val networkRequest = NetworkGenerateSongRequest(
            prompt = request.prompt,
            duration = request.durationInSeconds,
            test = request.isTest,
            structureId = request.structureId
        )
        
        val response = apiService.generateSong(networkRequest)
        
        return SongDto(
            id = response.id,
            title = response.title,
            durationInMillis = response.duration,
            musicFileUrl = response.musicFilePath,
            waveFormFileUrl = response.waveFormFilePath,
            createdAt = LocalDateTime.parse(response.createdAt, DateTimeFormatter.ISO_DATE_TIME),
            bpm = response.bpm,
            key = response.key?.let { keyResponse ->
                MusicKeyDto(
                    id = keyResponse.id,
                    name = keyResponse.name,
                    isActive = keyResponse.active
                )
            }
        )
    }

    override suspend fun getRandomPrompt(): RandomPromptDataDto {
        val response = apiService.getRandomPrompt()
        return RandomPromptDataDto(
            prompt = response.prompt
        )
    }
}
