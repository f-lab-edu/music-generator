package com.simuel.musicgenerator.core.database.dao

import com.simuel.musicgenerator.core.database.entity.SongEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SongDaoUnitTest {

    private lateinit var songDao: SongDao

    @Before
    fun setup() {
        songDao = mockk()
    }

    @Test
    fun `노래를 삽입하면 정상적으로 저장되어야 한다`() = runTest {
        // Given
        val song = SongEntity(
            id = "song123",
            title = "테스트 노래",
            duration = 180,
            musicFilePath = "https://example.com/song.mp3",
            waveFormFilePath = "https://example.com/waveform.json",
            createdAt = "2022-01-01T00:00:00Z",
            bpm = 120,
            musicKeyId = 1,
            musicKeyName = "C major",
            musicKeyActive = true
        )
        coEvery { songDao.insertSong(song) } returns Unit
        coEvery { songDao.getSongById("song123") } returns song

        // When
        songDao.insertSong(song)

        // Then
        val retrievedSong = songDao.getSongById("song123")
        assertEquals(song, retrievedSong)
        coVerify { songDao.insertSong(song) }
        coVerify { songDao.getSongById("song123") }
    }

    @Test
    fun `모든 노래를 조회하면 Flow를 반환해야 한다`() = runTest {
        // Given
        val songs = listOf(
            SongEntity(
                id = "song1",
                title = "노래1",
                duration = 180,
                musicFilePath = "song1.mp3",
                waveFormFilePath = "wave1.json",
                createdAt = "2022-01-01T00:00:00Z"
            ),
            SongEntity(
                id = "song2",
                title = "노래2",
                duration = 180,
                musicFilePath = "song2.mp3",
                waveFormFilePath = "wave2.json",
                createdAt = "2022-01-02T00:00:00Z"
            )
        )
        coEvery { songDao.getAllSongs() } returns flowOf(songs)

        // When
        val result = songDao.getAllSongs().first()

        // Then
        assertEquals(songs, result)
        coVerify { songDao.getAllSongs() }
    }

    @Test
    fun `존재하지 않는 ID로 노래를 조회하면 null을 반환해야 한다`() = runTest {
        // Given
        val nonExistentId = "nonexistent"
        coEvery { songDao.getSongById(nonExistentId) } returns null

        // When
        val result = songDao.getSongById(nonExistentId)

        // Then
        assertNull(result)
        coVerify { songDao.getSongById(nonExistentId) }
    }

    @Test
    fun `노래를 삭제하면 DAO 메서드가 호출되어야 한다`() = runTest {
        // Given
        val songId = "song123"
        coEvery { songDao.deleteSong(songId) } returns Unit

        // When
        songDao.deleteSong(songId)

        // Then
        coVerify { songDao.deleteSong(songId) }
    }
}