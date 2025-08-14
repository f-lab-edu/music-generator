package com.simuel.musicgenerator.domain.usecase.song

import com.simuel.musicgenerator.domain.model.Song
import com.simuel.musicgenerator.domain.model.SongGeneration
import com.simuel.musicgenerator.domain.repository.AccountRepository
import com.simuel.musicgenerator.domain.repository.SongRepository
import javax.inject.Inject

class GenerateAndSaveSongUseCase @Inject constructor(
    private val songRepository: SongRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(
        prompt: String,
        duration: Int? = null
    ): Song {
        require(prompt.isNotBlank()) { "프롬프트는 비어있을 수 없습니다" }
        require(prompt.length <= MAX_PROMPT_LENGTH) { 
            "프롬프트는 ${MAX_PROMPT_LENGTH}자를 초과할 수 없습니다" 
        }
        
        val limits = accountRepository.getAccountLimits()
        val generationLimit = limits.find { it.requestType == "generation" }
        require(generationLimit != null && generationLimit.left > 0) {
            "일일 생성 한도를 초과했습니다. 남은 횟수: ${generationLimit?.left ?: 0}"
        }
        
        val request = SongGeneration(
            prompt = prompt.trim(),
            durationInSeconds = duration
        )
        val song = songRepository.generateSong(request)
        
        songRepository.saveSong(song)
        
        return song
    }
    
    companion object {
        private const val MAX_PROMPT_LENGTH = 500
    }
}