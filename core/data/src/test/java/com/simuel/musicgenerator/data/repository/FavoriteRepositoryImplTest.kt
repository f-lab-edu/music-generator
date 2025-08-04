package com.simuel.musicgenerator.data.repository

import app.cash.turbine.test
import com.simuel.musicgenerator.data.datasource.FakeLoudlyLocalDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FavoriteRepositoryImplTest {

    private lateinit var fakeLocalDataSource: FakeLoudlyLocalDataSource
    private lateinit var repository: FavoriteRepositoryImpl

    @Before
    fun setup() {
        fakeLocalDataSource = FakeLoudlyLocalDataSource()
        repository = FavoriteRepositoryImpl(fakeLocalDataSource)
    }

    @Test
    fun `즐겨찾기에 추가하면 로컬 데이터소스에 저장되어야 한다`() = runTest {
        // Given
        val songId = "song123"

        // When
        repository.addToFavorites(songId)

        // Then
        val favorites = fakeLocalDataSource.getAllFavorites().first()
        assertEquals(1, favorites.size)
        assertEquals(songId, favorites[0].songId)
    }

    @Test
    fun `즐겨찾기에서 제거하면 로컬 데이터소스에서 삭제되어야 한다`() = runTest {
        // Given
        val songId = "song123"
        fakeLocalDataSource.addToFavorites(songId)
        
        // 추가 확인
        assertEquals(1, fakeLocalDataSource.getAllFavorites().first().size)

        // When
        repository.removeFromFavorites(songId)

        // Then
        val favorites = fakeLocalDataSource.getAllFavorites().first()
        assertEquals(0, favorites.size)
    }

    @Test
    fun `즐겨찾기 노래 상세 정보를 조회하면 즐겨찾기된 노래들의 상세 정보를 반환해야 한다`() = runTest {
        // Given
        fakeLocalDataSource.addTestSongs()
        fakeLocalDataSource.addToFavorites("song1")

        // When
        val result = repository.getFavoriteSongsWithDetails().first()

        // Then
        assertEquals(1, result.size)
        assertEquals("song1", result[0].id)
        assertEquals("Test Song 1", result[0].title)
    }

    @Test
    fun `즐겨찾기가 없을 때 빈 리스트를 반환해야 한다`() = runTest {
        // Given - 아무것도 설정하지 않음

        // When
        val result = repository.getFavoriteSongsWithDetails().first()

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `즐겨찾기 상세 조회 시 Flow가 업데이트되어야 한다`() = runTest {
        // Given
        fakeLocalDataSource.addTestSongs()

        // When & Then
        repository.getFavoriteSongsWithDetails().test {
            // 초기 상태 (빈 리스트)
            assertEquals(0, awaitItem().size)

            // 즐겨찾기 추가
            repository.addToFavorites("song1")
            assertEquals(1, awaitItem().size)

            // 즐겨찾기 제거
            repository.removeFromFavorites("song1")
            assertEquals(0, awaitItem().size)
        }
    }
}