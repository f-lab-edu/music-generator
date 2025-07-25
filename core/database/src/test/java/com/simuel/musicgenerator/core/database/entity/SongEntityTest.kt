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
    fun `동일한 ID를 가진 SongEntity는 equals에서 true를 반환해야 한다`() {
        // Given
        val id = "song123"
        val song1 = SongEntity(
            id = id,
            title = "노래1",
            duration = 180,
            musicFilePath = "path1.mp3",
            waveFormFilePath = "wave1.json",
            createdAt = "2022-01-01T00:00:00Z"
        )
        val song2 = SongEntity(
            id = id,
            title = "노래2",
            duration = 200,
            musicFilePath = "path2.mp3",
            waveFormFilePath = "wave2.json",
            createdAt = "2022-01-02T00:00:00Z"
        )

        // When & Then
        assertEquals(song1, song2)
        assertEquals(song1.hashCode(), song2.hashCode())
    }

}