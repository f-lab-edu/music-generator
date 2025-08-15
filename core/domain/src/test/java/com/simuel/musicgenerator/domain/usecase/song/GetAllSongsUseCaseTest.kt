package com.simuel.musicgenerator.domain.usecase.song

import com.simuel.musicgenerator.domain.model.Song
import com.simuel.musicgenerator.domain.model.SongGeneration
import com.simuel.musicgenerator.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAllSongsUseCaseTest {
    
    private lateinit var songRepository: FakeSongRepositoryForGetAll
    private lateinit var useCase: GetAllSongsUseCase
    
    @Before
    fun setup() {
        songRepository = FakeSongRepositoryForGetAll()
        useCase = GetAllSongsUseCase(songRepository)
    }
    
    @Test
    fun `모든 음악 조회 - 빈 리스트`() = runTest {
        // When
        val result = useCase().first()
        
        // Then
        assertEquals(emptyList<Song>(), result)
    }
    
    @Test
    fun `모든 음악 조회 - 여러 개의 음악`() = runTest {
        // Given
        val songs = listOf(
            createSong("song_1", "Song 1"),
            createSong("song_2", "Song 2"),
            createSong("song_3", "Song 3")
        )
        songRepository.setSongs(songs)
        
        // When
        val result = useCase().first()
        
        // Then
        assertEquals(3, result.size)
        assertEquals(songs, result)
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

// Test double
private class FakeSongRepositoryForGetAll : SongRepository {
    private var songs = emptyList<Song>()
    
    fun setSongs(songs: List<Song>) {
        this.songs = songs
    }
    
    override fun getAllSongs(): Flow<List<Song>> = flowOf(songs)
    
    override suspend fun generateSong(request: SongGeneration) = throw NotImplementedError()
    override suspend fun saveSong(song: Song) = throw NotImplementedError()
    override suspend fun getSongById(id: String) = throw NotImplementedError()
    override suspend fun deleteSong(id: String) = throw NotImplementedError()
}