package com.simuel.musicgenerator.domain.usecase.favorite

import com.simuel.musicgenerator.domain.model.Song
import com.simuel.musicgenerator.domain.repository.FakeFavoriteRepository
import com.simuel.musicgenerator.domain.repository.FakeSongRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AddToFavoritesUseCaseTest {
    
    private lateinit var favoriteRepository: FakeFavoriteRepository
    private lateinit var songRepository: FakeSongRepository
    private lateinit var useCase: AddToFavoritesUseCase
    
    @Before
    fun setup() {
        favoriteRepository = FakeFavoriteRepository()
        songRepository = FakeSongRepository()
        useCase = AddToFavoritesUseCase(favoriteRepository, songRepository)
    }
    
    @Test
    fun `즐겨찾기 추가 성공`() = runTest {
        // Given
        val songId = "song_1"
        val song = createSong(songId)
        songRepository.setSongById(songId, song)
        
        // When
        useCase(songId)
        
        // Then
        assertEquals(1, favoriteRepository.addCallCount)
        assertEquals(songId, favoriteRepository.lastAddedSongId)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `즐겨찾기 추가 실패 - 빈 ID`() = runTest {
        // Given
        val emptySongId = ""
        
        // When & Then
        useCase(emptySongId)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `즐겨찾기 추가 실패 - 존재하지 않는 음악`() = runTest {
        // Given
        val nonExistentId = "non_existent"
        
        // When & Then
        useCase(nonExistentId)
    }
    
    @Test
    fun `이미 즐겨찾기된 음악 추가 - 중복 허용`() = runTest {
        // Given
        val songId = "song_1"
        val song = createSong(songId)
        songRepository.setSongById(songId, song)
        favoriteRepository.addToFavorites(songId)
        
        // When
        useCase(songId)
        
        // Then
        assertEquals(2, favoriteRepository.addCallCount)
        assertEquals(songId, favoriteRepository.lastAddedSongId)
    }
    
    private fun createSong(id: String) = Song(
        id = id,
        title = "Test Song",
        duration = 30,
        musicFilePath = "https://example.com/song.mp3",
        waveFormFilePath = "https://example.com/waveform.png",
        createdAt = "2024-01-01T00:00:00Z",
        bpm = 120,
        musicKeyId = 1,
        musicKeyName = "C Major",
        musicKeyActive = true
    )
}