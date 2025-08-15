package com.simuel.musicgenerator.domain.usecase.song

import com.simuel.musicgenerator.domain.model.Song
import com.simuel.musicgenerator.domain.model.SongGeneration
import com.simuel.musicgenerator.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DeleteSongUseCaseTest {
    
    private lateinit var songRepository: FakeSongRepositoryForDelete
    private lateinit var useCase: DeleteSongUseCase
    
    @Before
    fun setup() {
        songRepository = FakeSongRepositoryForDelete()
        useCase = DeleteSongUseCase(songRepository)
    }
    
    @Test
    fun `음악 삭제 성공`() = runTest {
        // Given
        val songId = "song_1"
        val song = createSong(songId)
        songRepository.addSong(song)
        
        // When
        useCase(songId)
        
        // Then
        assertEquals(listOf(songId), songRepository.deletedSongIds)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `음악 삭제 실패 - 빈 ID`() = runTest {
        // Given
        val emptySongId = ""
        
        // When & Then
        useCase(emptySongId)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `음악 삭제 실패 - 존재하지 않는 음악`() = runTest {
        // Given
        val nonExistentId = "non_existent"
        
        // When & Then
        useCase(nonExistentId)
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

// Test double
private class FakeSongRepositoryForDelete : SongRepository {
    private val songs = mutableMapOf<String, Song>()
    val deletedSongIds = mutableListOf<String>()
    
    fun addSong(song: Song) {
        songs[song.id] = song
    }
    
    override suspend fun getSongById(id: String): Song? = songs[id]
    
    override suspend fun deleteSong(id: String) {
        deletedSongIds.add(id)
        songs.remove(id)
    }
    
    override suspend fun generateSong(request: SongGeneration) = throw NotImplementedError()
    override suspend fun saveSong(song: Song) = throw NotImplementedError()
    override fun getAllSongs(): Flow<List<Song>> = flowOf(songs.values.toList())
}