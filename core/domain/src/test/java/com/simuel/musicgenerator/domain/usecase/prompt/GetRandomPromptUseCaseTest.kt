package com.simuel.musicgenerator.domain.usecase.prompt

import com.simuel.musicgenerator.domain.model.RandomPromptData
import com.simuel.musicgenerator.domain.repository.FakePromptRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetRandomPromptUseCaseTest {
    
    private lateinit var promptRepository: FakePromptRepository
    private lateinit var useCase: GetRandomPromptUseCase
    
    @Before
    fun setup() {
        promptRepository = FakePromptRepository()
        useCase = GetRandomPromptUseCase(promptRepository)
    }
    
    @Test
    fun `랜덤 프롬프트 조회 성공`() = runTest {
        // Given
        val expectedPrompt = RandomPromptData(
            prompt = "Create an upbeat electronic dance music"
        )
        promptRepository.setRandomPrompt(expectedPrompt)
        
        // When
        val result = useCase()
        
        // Then
        assertEquals(expectedPrompt.prompt, result.prompt)
    }
    
    @Test
    fun `여러 번 호출 시 다른 프롬프트 반환`() = runTest {
        // Given
        val prompts = listOf(
            RandomPromptData("Jazz with saxophone"),
            RandomPromptData("Rock with electric guitar"),
            RandomPromptData("Classical piano sonata")
        )
        promptRepository.setMultiplePrompts(prompts)
        
        // When
        val result1 = useCase()
        val result2 = useCase()
        val result3 = useCase()
        
        // Then
        assertEquals(prompts[0].prompt, result1.prompt)
        assertEquals(prompts[1].prompt, result2.prompt)
        assertEquals(prompts[2].prompt, result3.prompt)
    }
    
    @Test
    fun `프롬프트 풀이 비어있을 때 기본값 반환`() = runTest {
        // Given - repository에 프롬프트를 설정하지 않음
        
        // When
        val result = useCase()
        
        // Then
        assertEquals("Default random prompt", result.prompt)
    }
}