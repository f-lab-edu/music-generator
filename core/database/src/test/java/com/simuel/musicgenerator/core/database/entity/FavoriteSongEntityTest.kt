package com.simuel.musicgenerator.core.database.entity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class FavoriteSongEntityTest {

    @Test
    fun `FavoriteSongEntity 생성 시 모든 필드가 올바르게 설정되어야 한다`() {
        // Given
        val songId = "song123"
        val addedAt = 1640995200000L // 2022-01-01 00:00:00

        // When
        val favoriteSongEntity = FavoriteSongEntity(
            songId = songId,
            addedAt = addedAt
        )

        // Then
        assertEquals(songId, favoriteSongEntity.songId)
        assertEquals(addedAt, favoriteSongEntity.addedAt)
    }

    @Test
    fun `FavoriteSongEntity는 기본 addedAt 값을 가져야 한다`() {
        // Given
        val songId = "song123"

        // When
        val favoriteSongEntity = FavoriteSongEntity(songId = songId)

        // Then
        assertEquals(songId, favoriteSongEntity.songId)
        assertNotNull(favoriteSongEntity.addedAt)
    }

    @Test
    fun `동일한 songId를 가진 FavoriteSongEntity는 equals에서 true를 반환해야 한다`() {
        // Given
        val songId = "song123"
        val favorite1 = FavoriteSongEntity(songId = songId, addedAt = 1000L)
        val favorite2 = FavoriteSongEntity(songId = songId, addedAt = 2000L)

        // When & Then
        assertEquals(favorite1, favorite2)
        assertEquals(favorite1.hashCode(), favorite2.hashCode())
    }
}