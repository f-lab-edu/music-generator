package com.simuel.musicgenerator.core.database.dao

import com.simuel.musicgenerator.core.database.entity.FavoriteSongEntity
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

class FavoriteSongDaoUnitTest {

    private lateinit var favoriteSongDao: FavoriteSongDao

    @Before
    fun setup() {
        favoriteSongDao = mockk()
    }

    @Test
    fun `즐겨찾기를 추가하면 DAO 메서드가 호출되어야 한다`() = runTest {
        // Given
        val favoriteSong = FavoriteSongEntity(songId = "song123", addedAt = 1640995200000L)
        coEvery { favoriteSongDao.addFavorite(favoriteSong) } returns Unit

        // When
        favoriteSongDao.addFavorite(favoriteSong)

        // Then
        coVerify { favoriteSongDao.addFavorite(favoriteSong) }
    }

    @Test
    fun `즐겨찾기를 삭제하면 DAO 메서드가 호출되어야 한다`() = runTest {
        // Given
        val songId = "song123"
        coEvery { favoriteSongDao.removeFavorite(songId) } returns Unit

        // When
        favoriteSongDao.removeFavorite(songId)

        // Then
        coVerify { favoriteSongDao.removeFavorite(songId) }
    }

    @Test
    fun `즐겨찾기 여부를 확인할 수 있어야 한다`() = runTest {
        // Given
        val songId = "song123"
        coEvery { favoriteSongDao.isFavorite(songId) } returns true

        // When
        val result = favoriteSongDao.isFavorite(songId)

        // Then
        assertTrue(result)
        coVerify { favoriteSongDao.isFavorite(songId) }
    }

    @Test
    fun `즐겨찾기가 아닌 노래를 확인하면 false를 반환해야 한다`() = runTest {
        // Given
        val songId = "song123"
        coEvery { favoriteSongDao.isFavorite(songId) } returns false

        // When
        val result = favoriteSongDao.isFavorite(songId)

        // Then
        assertFalse(result)
        coVerify { favoriteSongDao.isFavorite(songId) }
    }

    @Test
    fun `모든 즐겨찾기를 조회하면 Flow를 반환해야 한다`() = runTest {
        // Given
        val favorites = listOf(
            FavoriteSongEntity(songId = "song1", addedAt = 1000L),
            FavoriteSongEntity(songId = "song2", addedAt = 2000L)
        )
        coEvery { favoriteSongDao.getAllFavorites() } returns flowOf(favorites)

        // When
        val result = favoriteSongDao.getAllFavorites().first()

        // Then
        assertEquals(favorites, result)
        coVerify { favoriteSongDao.getAllFavorites() }
    }
}