package com.simuel.musicgenerator.domain.usecase.prompt

import com.simuel.musicgenerator.domain.model.RandomPromptData
import com.simuel.musicgenerator.domain.repository.PromptRepository
import javax.inject.Inject

class GetRandomPromptUseCase @Inject constructor(
    private val promptRepository: PromptRepository
) {
    suspend operator fun invoke(): RandomPromptData {
        return promptRepository.getRandomPrompt()
    }
}