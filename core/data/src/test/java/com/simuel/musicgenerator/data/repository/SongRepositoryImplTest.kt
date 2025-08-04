package com.simuel.musicgenerator.data.repository

import app.cash.turbine.test
import com.simuel.musicgenerator.data.datasource.FakeLoudlyLocalDataSource
import com.simuel.musicgenerator.data.datasource.FakeLoudlyRemoteDataSource
import com.simuel.musicgenerator.domain.model.Song
import com.simuel.musicgenerator.domain.model.SongGeneration
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class SongRepositoryImplTest {

    private lateinit var fakeRemoteDataSource: FakeLoudlyRemoteDataSource
    private lateinit var fakeLocalDataSource: FakeLoudlyLocalDataSource
    private lateinit var repository: SongRepositoryImpl

    @Before
    fun setup() {
        fakeRemoteDataSource = FakeLoudlyRemoteDataSource()
        fakeLocalDataSource = FakeLoudlyLocalDataSource()
        repository = SongRepositoryImpl(fakeRemoteDataSource, fakeLocalDataSource)
    }

    @Test
    fun `노래 생성 요청 시 원격 데이터소스에서 생성하고 로컬에 저장해야 한다`() = runTest {
        // Given
        val songGeneration = SongGeneration(
            prompt = "Test prompt",
            durationInSeconds = 60,
            isTest = false,
            structureId = null
        )

        // When
        val result = repository.generateSong(songGeneration)

        // Then
        assertNotNull(result)
        assertEquals("test-song-id", result.id)
        assertEquals("Generated Song: Test prompt", result.title)
        assertEquals(60000, result.duration)
        
        // 로컬 데이터소스에 저장되었는지 확인
        val savedSongs = fakeLocalDataSource.getAllSongs().first()
        assertEquals(1, savedSongs.size)
        assertEquals("test-song-id", savedSongs[0].id)
    }

    @Test
    fun `모든 노래를 조회하면 로컬 데이터소스의 데이터를 반환해야 한다`() = runTest {
        // Given
        fakeLocalDataSource.addTestSongs()

        // When
        val result = repository.getAllSongs().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("song1", result[0].id)
        assertEquals("song2", result[1].id)
    }

    @Test
    fun `ID로 노래를 조회하면 해당 노래를 반환해야 한다`() = runTest {
        // Given
        fakeLocalDataSource.addTestSongs()
        val songId = "song1"

        // When
        val result = repository.getSongById(songId)

        // Then
        assertNotNull(result)
        assertEquals(songId, result!!.id)
        assertEquals("Test Song 1", result.title)
    }

    @Test
    fun `존재하지 않는 ID로 노래를 조회하면 null을 반환해야 한다`() = runTest {
        // Given
        val songId = "nonexistent-song"

        // When
        val result = repository.getSongById(songId)

        // Then
        assertNull(result)
    }

    @Test
    fun `노래를 삭제하면 로컬 데이터소스에서 삭제되어야 한다`() = runTest {
        // Given
        fakeLocalDataSource.addTestSongs()
        val songId = "song1"

        // 삭제 전 확인
        assertEquals(2, fakeLocalDataSource.getAllSongs().first().size)

        // When
        repository.deleteSong(songId)

        // Then
        val remainingSongs = fakeLocalDataSource.getAllSongs().first()
        assertEquals(1, remainingSongs.size)
        assertEquals("song2", remainingSongs[0].id)
    }

    @Test
    fun `노래를 저장하면 로컬 데이터소스에 저장되어야 한다`() = runTest {
        // Given
        val song = Song(
            id = "test-song",
            title = "Test Song",
            duration = 180000,
            musicFilePath = "test.mp3",
            waveFormFilePath = "test.json",
            createdAt = "2023-01-01T00:00:00Z",
            bpm = 120,
            musicKeyId = 1,
            musicKeyName = "C major",
            musicKeyActive = true
        )

        // When
        repository.saveSong(song)

        // Then
        val savedSongs = fakeLocalDataSource.getAllSongs().first()
        assertEquals(1, savedSongs.size)
        assertEquals("test-song", savedSongs[0].id)
        assertEquals("Test Song", savedSongs[0].title)
    }

    @Test
    fun `모든 노래 조회 시 Flow가 업데이트되어야 한다`() = runTest {
        // Given & When & Then
        repository.getAllSongs().test {
            // 초기 상태 (빈 리스트)
            assertEquals(0, awaitItem().size)

            // 노래 추가
            fakeLocalDataSource.addTestSongs()
            assertEquals(2, awaitItem().size)

            // 노래 삭제
            repository.deleteSong("song1")
            assertEquals(1, awaitItem().size)
        }
    }
}