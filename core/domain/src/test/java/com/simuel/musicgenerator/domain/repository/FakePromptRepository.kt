package com.simuel.musicgenerator.domain.repository

import com.simuel.musicgenerator.domain.model.RandomPromptData

class FakePromptRepository : PromptRepository {
    private var prompts = mutableListOf<RandomPromptData>()
    private var currentIndex = 0
    
    fun setRandomPrompt(prompt: RandomPromptData) {
        prompts.clear()
        prompts.add(prompt)
        currentIndex = 0
    }
    
    fun setMultiplePrompts(promptList: List<RandomPromptData>) {
        prompts.clear()
        prompts.addAll(promptList)
        currentIndex = 0
    }
    
    override suspend fun getRandomPrompt(): RandomPromptData {
        return if (prompts.isEmpty()) {
            // 기본값 반환
            RandomPromptData(
                prompt = "Default random prompt"
            )
        } else {
            val prompt = prompts[currentIndex % prompts.size]
            currentIndex++
            prompt
        }
    }
}