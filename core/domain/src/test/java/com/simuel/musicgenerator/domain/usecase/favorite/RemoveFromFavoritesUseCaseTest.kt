package com.simuel.musicgenerator.domain.usecase.favorite

import com.simuel.musicgenerator.domain.repository.FakeFavoriteRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class RemoveFromFavoritesUseCaseTest {
    
    private lateinit var favoriteRepository: FakeFavoriteRepository
    private lateinit var useCase: RemoveFromFavoritesUseCase
    
    @Before
    fun setup() {
        favoriteRepository = FakeFavoriteRepository()
        useCase = RemoveFromFavoritesUseCase(favoriteRepository)
    }
    
    @Test
    fun `즐겨찾기에서 음악 제거 성공`() = runTest {
        // Given
        val songId = "song_123"
        
        // When
        useCase(songId)
        
        // Then
        assertEquals(1, favoriteRepository.removeCallCount)
        assertEquals("song_123", favoriteRepository.lastRemovedSongId)
    }
    
    @Test
    fun `존재하지 않는 음악 즐겨찾기 제거 - 에러 없이 처리`() = runTest {
        // Given
        val nonExistentSongId = "non_existent_song"
        
        // When
        useCase(nonExistentSongId)
        
        // Then - 에러 없이 정상 처리
        assertEquals(1, favoriteRepository.removeCallCount)
        assertEquals("non_existent_song", favoriteRepository.lastRemovedSongId)
    }
    
    @Test
    fun `빈 Song ID로 즐겨찾기 제거 시도 - 예외 발생`() {
        // When & Then
        assertThrows(IllegalArgumentException::class.java) {
            runTest {
                useCase("")
            }
        }
    }
    
    @Test
    fun `공백만 있는 Song ID로 즐겨찾기 제거 시도 - 예외 발생`() {
        // When & Then
        assertThrows(IllegalArgumentException::class.java) {
            runTest {
                useCase("   ")
            }
        }
    }
    
    @Test
    fun `동일한 음악 중복 제거 시도`() = runTest {
        // Given
        val songId = "song_123"
        
        // When
        useCase(songId)
        useCase(songId) // 중복 제거
        
        // Then
        assertEquals(2, favoriteRepository.removeCallCount)
        assertEquals("song_123", favoriteRepository.lastRemovedSongId)
    }
    
    @Test
    fun `여러 번 다른 음악 제거`() = runTest {
        // When
        useCase("song_1")
        useCase("song_2")
        useCase("song_3")
        
        // Then
        assertEquals(3, favoriteRepository.removeCallCount)
        assertEquals("song_3", favoriteRepository.lastRemovedSongId)
    }
}