package com.simuel.musicgenerator.core.database.entity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class SongEntityTest {

    @Test
    fun `SongEntity 생성 시 모든 필드가 올바르게 설정되어야 한다`() {
        // Given
        val id = "song123"
        val title = "테스트 노래"
        val duration = 180
        val musicFilePath = "https://example.com/song.mp3"
        val waveFormFilePath = "https://example.com/waveform.json"
        val createdAt = "2022-01-01T00:00:00Z"
        val bpm = 120
        val musicKeyId = 1
        val musicKeyName = "C major"
        val musicKeyActive = true

        // When
        val songEntity = SongEntity(
            id = id,
            title = title,
            duration = duration,
            musicFilePath = musicFilePath,
            waveFormFilePath = waveFormFilePath,
            createdAt = createdAt,
            bpm = bpm,
            musicKeyId = musicKeyId,
            musicKeyName = musicKeyName,
            musicKeyActive = musicKeyActive
        )

        // Then
        assertEquals(id, songEntity.id)
        assertEquals(title, songEntity.title)
        assertEquals(duration, songEntity.duration)
        assertEquals(musicFilePath, songEntity.musicFilePath)
        assertEquals(waveFormFilePath, songEntity.waveFormFilePath)
        assertEquals(createdAt, songEntity.createdAt)
        assertEquals(bpm, songEntity.bpm)
        assertEquals(musicKeyId, songEntity.musicKeyId)
        assertEquals(musicKeyName, songEntity.musicKeyName)
        assertEquals(musicKeyActive, songEntity.musicKeyActive)
    }

    @Test
    fun `SongEntity는 nullable 필드들을 null로 설정할 수 있어야 한다`() {
        // Given
        val id = "song123"
        val title = "테스트 노래"
        val duration = 180
        val musicFilePath = "https://example.com/song.mp3"
        val waveFormFilePath = "https://example.com/waveform.json"
        val createdAt = "2022-01-01T00:00:00Z"

        // When
        val songEntity = SongEntity(
            id = id,
            title = title,
            duration = duration,
            musicFilePath = musicFilePath,
            waveFormFilePath = waveFormFilePath,
            createdAt = createdAt,
            bpm = null,
            musicKeyId = null,
            musicKeyName = null,
            musicKeyActive = null
        )

        // Then
        assertEquals(id, songEntity.id)
        assertEquals(title, songEntity.title)
        assertEquals(duration, songEntity.duration)
        assertEquals(musicFilePath, songEntity.musicFilePath)
        assertEquals(waveFormFilePath, songEntity.waveFormFilePath)
        assertEquals(createdAt, songEntity.createdAt)
        assertNull(songEntity.bpm)
        assertNull(songEntity.musicKeyId)
        assertNull(songEntity.musicKeyName)
        assertNull(songEntity.musicKeyActive)
    }

    @Test
    fun `SongEntity의 copy 함수가 올바르게 동작해야 한다`() {
        // Given
        val original = SongEntity(
            id = "song123",
            title = "원본 노래",
            duration = 180,
            musicFilePath = "original.mp3",
            waveFormFilePath = "original.json",
            createdAt = "2022-01-01T00:00:00Z",
            bpm = 120,
            musicKeyId = 1,
            musicKeyName = "C major",
            musicKeyActive = true
        )

        // When
        val copied = original.copy(
            title = "수정된 노래",
            duration = 200
        )

        // Then
        assertEquals("song123", copied.id)
        assertEquals("수정된 노래", copied.title)
        assertEquals(200, copied.duration)
        assertEquals("original.mp3", copied.musicFilePath)
        assertEquals("original.json", copied.waveFormFilePath)
        assertEquals("2022-01-01T00:00:00Z", copied.createdAt)
        assertEquals(120, copied.bpm)
        assertEquals(1, copied.musicKeyId)
        assertEquals("C major", copied.musicKeyName)
        assertEquals(true, copied.musicKeyActive)
    }

}