package com.simuel.musicgenerator.domain.usecase.favorite

import com.simuel.musicgenerator.domain.model.Song
import com.simuel.musicgenerator.domain.repository.FakeFavoriteRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetFavoriteSongsUseCaseTest {
    
    private lateinit var favoriteRepository: FakeFavoriteRepository
    private lateinit var useCase: GetFavoriteSongsUseCase
    
    @Before
    fun setup() {
        favoriteRepository = FakeFavoriteRepository()
        useCase = GetFavoriteSongsUseCase(favoriteRepository)
    }
    
    @Test
    fun `즐겨찾기 음악 조회 - 빈 리스트`() = runTest {
        // Given
        favoriteRepository.setFavoriteSongs(emptyList())
        
        // When
        val result = useCase().first()
        
        // Then
        assertTrue(result.isEmpty())
    }
    
    @Test
    fun `즐겨찾기 음악 조회 - 단일 음악`() = runTest {
        // Given
        val favoriteSong = createSong("song_1", "Favorite Song 1")
        favoriteRepository.setFavoriteSongs(listOf(favoriteSong))
        
        // When
        val result = useCase().first()
        
        // Then
        assertEquals(1, result.size)
        assertEquals(favoriteSong, result[0])
    }
    
    @Test
    fun `즐겨찾기 음악 조회 - 여러 개의 음악`() = runTest {
        // Given
        val favoriteSongs = listOf(
            createSong("song_1", "Favorite Song 1"),
            createSong("song_2", "Favorite Song 2"),
            createSong("song_3", "Favorite Song 3"),
            createSong("song_4", "Favorite Song 4"),
            createSong("song_5", "Favorite Song 5")
        )
        favoriteRepository.setFavoriteSongs(favoriteSongs)
        
        // When
        val result = useCase().first()
        
        // Then
        assertEquals(5, result.size)
        assertEquals(favoriteSongs, result)
    }
    
    @Test
    fun `즐겨찾기 음악 조회 - Flow 스트림 확인`() = runTest {
        // Given
        val initialSongs = listOf(
            createSong("song_1", "Song 1")
        )
        favoriteRepository.setFavoriteSongs(initialSongs)
        
        // When
        val flow = useCase()
        val firstEmission = flow.first()
        
        // Then
        assertEquals(1, firstEmission.size)
        assertEquals("song_1", firstEmission[0].id)
    }
    
    @Test
    fun `즐겨찾기 음악 정렬 순서 확인`() = runTest {
        // Given - 역순으로 추가
        val songs = listOf(
            createSong("song_5", "Song 5"),
            createSong("song_4", "Song 4"),
            createSong("song_3", "Song 3"),
            createSong("song_2", "Song 2"),
            createSong("song_1", "Song 1")
        )
        favoriteRepository.setFavoriteSongs(songs)
        
        // When
        val result = useCase().first()
        
        // Then - 입력된 순서 그대로 반환
        assertEquals("song_5", result[0].id)
        assertEquals("song_4", result[1].id)
        assertEquals("song_3", result[2].id)
        assertEquals("song_2", result[3].id)
        assertEquals("song_1", result[4].id)
    }
    
    private fun createSong(id: String, title: String) = Song(
        id = id,
        title = title,
        duration = 30,
        musicFilePath = "https://example.com/$id.mp3",
        waveFormFilePath = "https://example.com/$id.png",
        createdAt = "2024-01-01T00:00:00Z",
        bpm = 120,
        musicKeyId = 1,
        musicKeyName = "C Major",
        musicKeyActive = true
    )
}
