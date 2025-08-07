package com.simuel.musicgenerator.data.repository

import com.simuel.musicgenerator.data.datasource.LoudlyRemoteDataSource
import com.simuel.musicgenerator.domain.model.RandomPromptData
import com.simuel.musicgenerator.domain.repository.PromptRepository
import javax.inject.Inject

internal class PromptRepositoryImpl @Inject constructor(
    private val remoteDataSource: LoudlyRemoteDataSource
) : PromptRepository {

    override suspend fun getRandomPrompt(): RandomPromptData {
        val promptDto = remoteDataSource.getRandomPrompt()
        
        return RandomPromptData(
            prompt = promptDto.prompt
        )
    }
}