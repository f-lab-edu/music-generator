package com.simuel.musicgenerator.domain.repository

import com.simuel.musicgenerator.domain.model.RandomPromptData

interface PromptRepository {
    suspend fun getRandomPrompt(): RandomPromptData
}