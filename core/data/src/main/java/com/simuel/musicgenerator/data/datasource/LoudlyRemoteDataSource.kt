package com.simuel.musicgenerator.data.datasource

import com.simuel.musicgenerator.data.model.AccountLimitInfoDto
import com.simuel.musicgenerator.data.model.SongGenerationDto
import com.simuel.musicgenerator.data.model.RandomPromptDataDto
import com.simuel.musicgenerator.data.model.SongDto

interface LoudlyRemoteDataSource {
    suspend fun getAccountLimits(): List<AccountLimitInfoDto>
    suspend fun generateSong(request: SongGenerationDto): SongDto
    suspend fun getRandomPrompt(): RandomPromptDataDto
}
