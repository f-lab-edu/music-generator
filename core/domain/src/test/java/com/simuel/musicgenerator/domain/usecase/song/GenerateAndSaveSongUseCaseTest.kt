package com.simuel.musicgenerator.domain.usecase.song

import com.simuel.musicgenerator.domain.model.AccountLimitInfo
import com.simuel.musicgenerator.domain.model.Song
import com.simuel.musicgenerator.domain.model.SongGeneration
import com.simuel.musicgenerator.domain.repository.AccountRepository
import com.simuel.musicgenerator.domain.repository.SongRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GenerateAndSaveSongUseCaseTest {
    
    private lateinit var songRepository: FakeSongRepositoryForGenerate
    private lateinit var accountRepository: FakeAccountRepository
    private lateinit var useCase: GenerateAndSaveSongUseCase
    
    @Before
    fun setup() {
        songRepository = FakeSongRepositoryForGenerate()
        accountRepository = FakeAccountRepository()
        useCase = GenerateAndSaveSongUseCase(songRepository, accountRepository)
    }
    
    @Test
    fun `음악 생성 성공 - 정상적인 프롬프트와 충분한 한도`() = runTest {
        // Given
        val prompt = "Create a happy song"
        val duration = 30
        accountRepository.setLimits(
            listOf(
                AccountLimitInfo(
                    requestType = "generation",
                    limit = 10,
                    used = 5,
                    left = 5,
                    dateFrom = "2024-01-01",
                    dateTo = "2024-01-31"
                )
            )
        )
        
        // When
        val result = useCase(prompt, duration)
        
        // Then
        assertEquals("Generated Song", result.title)
        assertEquals(duration, result.duration)
        assertEquals(1, songRepository.savedSongs.size)
        assertEquals(result, songRepository.savedSongs.first())
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `음악 생성 실패 - 빈 프롬프트`() = runTest {
        // Given
        val emptyPrompt = ""
        
        // When & Then
        useCase(emptyPrompt)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `음악 생성 실패 - 프롬프트 길이 초과`() = runTest {
        // Given
        val longPrompt = "a".repeat(501)
        
        // When & Then
        useCase(longPrompt)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `음악 생성 실패 - 생성 한도 초과`() = runTest {
        // Given
        val prompt = "Create a song"
        accountRepository.setLimits(
            listOf(
                AccountLimitInfo(
                    requestType = "generation",
                    limit = 10,
                    used = 10,
                    left = 0,
                    dateFrom = "2024-01-01",
                    dateTo = "2024-01-31"
                )
            )
        )
        
        // When & Then
        useCase(prompt)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `음악 생성 실패 - 생성 한도 정보 없음`() = runTest {
        // Given
        val prompt = "Create a song"
        accountRepository.setLimits(emptyList())
        
        // When & Then
        useCase(prompt)
    }
    
    @Test
    fun `프롬프트 앞뒤 공백 제거`() = runTest {
        // Given
        val promptWithSpaces = "  Create a song  "
        accountRepository.setLimits(
            listOf(
                AccountLimitInfo(
                    requestType = "generation",
                    limit = 10,
                    used = 5,
                    left = 5,
                    dateFrom = "2024-01-01",
                    dateTo = "2024-01-31"
                )
            )
        )
        
        // When
        val result = useCase(promptWithSpaces)
        
        // Then
        assertEquals("Generated Song", result.title)
    }
}

// Test doubles
private class FakeSongRepositoryForGenerate : SongRepository {
    val savedSongs = mutableListOf<Song>()
    private var songIdCounter = 1
    
    override suspend fun generateSong(request: SongGeneration): Song {
        return Song(
            id = "song_${songIdCounter++}",
            title = "Generated Song",
            duration = request.durationInSeconds ?: 30,
            musicFilePath = "https://example.com/song.mp3",
            waveFormFilePath = "https://example.com/waveform.png",
            createdAt = "2024-01-01T00:00:00Z",
            bpm = 120,
            musicKeyId = 1,
            musicKeyName = "C Major",
            musicKeyActive = true
        )
    }
    
    override suspend fun saveSong(song: Song) {
        savedSongs.add(song)
    }
    
    override fun getAllSongs() = throw NotImplementedError()
    override suspend fun getSongById(id: String) = throw NotImplementedError()
    override suspend fun deleteSong(id: String) = throw NotImplementedError()
}

private class FakeAccountRepository : AccountRepository {
    private var limits = emptyList<AccountLimitInfo>()
    
    fun setLimits(limits: List<AccountLimitInfo>) {
        this.limits = limits
    }
    
    override suspend fun getAccountLimits(): List<AccountLimitInfo> = limits
}