package com.simuel.musicgenerator.core.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.simuel.musicgenerator.core.database.db.MusicGeneratorDatabase
import com.simuel.musicgenerator.core.database.entity.SongEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SongDaoIntegrationTest {

    private lateinit var database: MusicGeneratorDatabase
    private lateinit var songDao: SongDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), MusicGeneratorDatabase::class.java
        ).allowMainThreadQueries().build()

        songDao = database.songDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun 노래를_삽입하고_조회할_수_있어야_한다() = runTest {
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

        // When
        songDao.insertSong(song)

        // Then
        val songs = songDao.getAllSongs().first()
        assertEquals(1, songs.size)
        assertEquals(song.id, songs[0].id)
        assertEquals(song.title, songs[0].title)
        assertEquals(song.duration, songs[0].duration)
        assertEquals(song.musicKeyName, songs[0].musicKeyName)
    }

    @Test
    fun 노래_목록이_생성일_기준_내림차순으로_정렬되어야_한다() = runTest {
        // Given
        val song1 = SongEntity(
            id = "song1",
            title = "노래1",
            duration = 180,
            musicFilePath = "song1.mp3",
            waveFormFilePath = "wave1.json",
            createdAt = "2022-01-01T00:00:00Z"
        )
        val song2 = SongEntity(
            id = "song2",
            title = "노래2",
            duration = 180,
            musicFilePath = "song2.mp3",
            waveFormFilePath = "wave2.json",
            createdAt = "2022-01-03T00:00:00Z"
        )
        val song3 = SongEntity(
            id = "song3",
            title = "노래3",
            duration = 180,
            musicFilePath = "song3.mp3",
            waveFormFilePath = "wave3.json",
            createdAt = "2022-01-02T00:00:00Z"
        )

        // When
        songDao.insertSong(song1)
        songDao.insertSong(song2)
        songDao.insertSong(song3)

        // Then
        val songs = songDao.getAllSongs().first()
        assertEquals(3, songs.size)
        assertEquals("song2", songs[0].id) // 가장 최근
        assertEquals("song3", songs[1].id) // 중간
        assertEquals("song1", songs[2].id) // 가장 오래된
    }

    @Test
    fun 노래를_삭제할_수_있어야_한다() = runTest {
        // Given
        val song = SongEntity(
            id = "song123",
            title = "테스트 노래",
            duration = 180,
            musicFilePath = "test.mp3",
            waveFormFilePath = "test.json",
            createdAt = "2022-01-01T00:00:00Z"
        )

        // When
        songDao.insertSong(song)
        songDao.deleteSong("song123")

        // Then
        val songs = songDao.getAllSongs().first()
        assertTrue(songs.isEmpty())
    }

    @Test
    fun 빈_데이터베이스에서_조회하면_빈_목록을_반환해야_한다() = runTest {
        // When
        val songs = songDao.getAllSongs().first()

        // Then
        assertTrue(songs.isEmpty())
    }
}
