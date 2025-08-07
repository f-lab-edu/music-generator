package com.simuel.musicgenerator.data.datasource

import com.simuel.musicgenerator.data.model.FavoriteSongDto
import com.simuel.musicgenerator.data.model.SongDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeLoudlyLocalDataSource : LoudlyLocalDataSource {

    private val songs = MutableStateFlow<List<SongDto>>(emptyList())
    private val favorites = MutableStateFlow<List<FavoriteSongDto>>(emptyList())

    override suspend fun saveSong(song: SongDto) {
        val currentSongs = songs.value.toMutableList()
        val existingIndex = currentSongs.indexOfFirst { it.id == song.id }
        if (existingIndex != -1) {
            currentSongs[existingIndex] = song
        } else {
            currentSongs.add(song)
        }
        songs.value = currentSongs
    }

    override fun getAllSongs(): Flow<List<SongDto>> {
        return songs
    }

    override suspend fun getSongById(id: String): SongDto? {
        return songs.value.find { it.id == id }
    }

    override suspend fun deleteSong(id: String) {
        songs.value = songs.value.filter { it.id != id }
    }

    override suspend fun addToFavorites(songId: String) {
        val currentFavorites = favorites.value.toMutableList()
        if (currentFavorites.none { it.songId == songId }) {
            currentFavorites.add(FavoriteSongDto(songId = songId))
            favorites.value = currentFavorites
        }
    }

    override suspend fun removeFromFavorites(songId: String) {
        favorites.value = favorites.value.filter { it.songId != songId }
    }

    override fun getAllFavorites(): Flow<List<FavoriteSongDto>> {
        return favorites
    }

    override fun getFavoriteSongsWithDetails(): Flow<List<SongDto>> {
        return favorites.map { favoriteDtos ->
            favoriteDtos.mapNotNull { favoriteDto ->
                songs.value.find { it.id == favoriteDto.songId }
            }
        }
    }

    fun addTestSongs() {
        val testSongs = listOf(
            SongDto(
                id = "song1",
                title = "Test Song 1",
                duration = 180000,
                musicFilePath = "song1.mp3",
                waveFormFilePath = "wave1.json",
                createdAt = "2023-01-01T00:00:00Z",
                bpm = 120,
                musicKeyId = 1,
                musicKeyName = "C major",
                musicKeyActive = true
            ),
            SongDto(
                id = "song2",
                title = "Test Song 2",
                duration = 240000,
                musicFilePath = "song2.mp3",
                waveFormFilePath = "wave2.json",
                createdAt = "2023-01-02T00:00:00Z",
                bpm = 140,
                musicKeyId = 2,
                musicKeyName = "D minor",
                musicKeyActive = true
            )
        )
        songs.value = testSongs
    }
}
