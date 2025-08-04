package com.simuel.musicgenerator.core.database.datasource

import com.simuel.musicgenerator.core.database.dao.FavoriteSongDao
import com.simuel.musicgenerator.core.database.dao.SongDao
import com.simuel.musicgenerator.core.database.entity.FavoriteSongEntity
import com.simuel.musicgenerator.core.database.entity.SongEntity
import com.simuel.musicgenerator.data.model.FavoriteSongDto
import com.simuel.musicgenerator.data.model.SongDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoudlyLocalDataSourceImplTest {

    private lateinit var songDao: SongDao
    private lateinit var favoriteSongDao: FavoriteSongDao
    private lateinit var localDataSource: LoudlyLocalDataSourceImpl

    @Before
    fun setup() {
        songDao = mockk()
        favoriteSongDao = mockk()
        localDataSource = LoudlyLocalDataSourceImpl(songDao, favoriteSongDao)
    }

    @Test
    fun `노래를 저장하면 DAO를 통해 삽입되어야 한다`() = runTest {
        // Given
        val song = SongDto(
            id = "song123",
            title = "테스트 노래",
            duration = 180,
            musicFilePath = "test.mp3",
            waveFormFilePath = "test.json",
            createdAt = "2022-01-01T00:00:00Z",
            bpm = null,
            musicKeyId = null,
            musicKeyName = null,
            musicKeyActive = null
        )
        coEvery { songDao.insertSong(any()) } returns Unit

        // When
        localDataSource.saveSong(song)

        // Then
        coVerify { songDao.insertSong(match { it.id == song.id }) }
    }


    @Test
    fun `모든 노래를 조회하면 DAO의 Flow를 반환해야 한다`() = runTest {
        // Given
        val songEntities = listOf(
            SongEntity(
                id = "song1",
                title = "노래1",
                duration = 180,
                musicFilePath = "song1.mp3",
                waveFormFilePath = "wave1.json",
                createdAt = "2022-01-01T00:00:00Z",
                bpm = null,
                musicKeyId = null,
                musicKeyName = null,
                musicKeyActive = null
            ),
            SongEntity(
                id = "song2",
                title = "노래2",
                duration = 180,
                musicFilePath = "song2.mp3",
                waveFormFilePath = "wave2.json",
                createdAt = "2022-01-01T00:00:00Z",
                bpm = null,
                musicKeyId = null,
                musicKeyName = null,
                musicKeyActive = null
            )
        )
        val expectedSongs = listOf(
            SongDto(
                id = "song1",
                title = "노래1",
                duration = 180,
                musicFilePath = "song1.mp3",
                waveFormFilePath = "wave1.json",
                createdAt = "2022-01-01T00:00:00Z",
                bpm = null,
                musicKeyId = null,
                musicKeyName = null,
                musicKeyActive = null
            ),
            SongDto(
                id = "song2",
                title = "노래2",
                duration = 180,
                musicFilePath = "song2.mp3",
                waveFormFilePath = "wave2.json",
                createdAt = "2022-01-01T00:00:00Z",
                bpm = null,
                musicKeyId = null,
                musicKeyName = null,
                musicKeyActive = null
            )
        )
        coEvery { songDao.getAllSongs() } returns flowOf(songEntities)

        // When
        val result = localDataSource.getAllSongs().first()

        // Then
        assertEquals(expectedSongs, result)
        coVerify { songDao.getAllSongs() }
    }

    @Test
    fun `노래를 삭제하면 DAO를 통해 삭제되어야 한다`() = runTest {
        // Given
        val songId = "song123"
        coEvery { songDao.deleteSong(songId) } returns Unit

        // When
        localDataSource.deleteSong(songId)

        // Then
        coVerify { songDao.deleteSong(songId) }
    }

    @Test
    fun `즐겨찾기를 추가하면 DAO를 통해 삽입되어야 한다`() = runTest {
        // Given
        val songId = "song123"
        coEvery { favoriteSongDao.addFavorite(any()) } returns Unit

        // When
        localDataSource.addToFavorites(songId)

        // Then
        coVerify { 
            favoriteSongDao.addFavorite(match { 
                it.songId == songId 
            })
        }
    }

    @Test
    fun `즐겨찾기를 제거하면 DAO를 통해 삭제되어야 한다`() = runTest {
        // Given
        val songId = "song123"
        coEvery { favoriteSongDao.removeFavorite(songId) } returns Unit

        // When
        localDataSource.removeFromFavorites(songId)

        // Then
        coVerify { favoriteSongDao.removeFavorite(songId) }
    }


    @Test
    fun `모든 즐겨찾기를 조회하면 DAO의 Flow를 반환해야 한다`() = runTest {
        // Given
        val favoriteEntities = listOf(
            FavoriteSongEntity(songId = "song1"),
            FavoriteSongEntity(songId = "song2")
        )
        val expectedFavorites = listOf(
            FavoriteSongDto(songId = "song1"),
            FavoriteSongDto(songId = "song2")
        )
        coEvery { favoriteSongDao.getAllFavorites() } returns flowOf(favoriteEntities)

        // When
        val result = localDataSource.getAllFavorites().first()

        // Then
        assertEquals(expectedFavorites, result)
        coVerify { favoriteSongDao.getAllFavorites() }
    }

    @Test
    fun `빈 노래 목록을 조회하면 빈 리스트를 반환해야 한다`() = runTest {
        // Given
        coEvery { songDao.getAllSongs() } returns flowOf(emptyList())

        // When
        val result = localDataSource.getAllSongs().first()

        // Then
        assertTrue(result.isEmpty())
        coVerify { songDao.getAllSongs() }
    }

    @Test
    fun `빈 즐겨찾기 목록을 조회하면 빈 리스트를 반환해야 한다`() = runTest {
        // Given
        coEvery { favoriteSongDao.getAllFavorites() } returns flowOf(emptyList())

        // When
        val result = localDataSource.getAllFavorites().first()

        // Then
        assertTrue(result.isEmpty())
        coVerify { favoriteSongDao.getAllFavorites() }
    }
}
