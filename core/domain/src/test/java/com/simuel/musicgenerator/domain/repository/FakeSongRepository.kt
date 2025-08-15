package com.simuel.musicgenerator.domain.repository

import com.simuel.musicgenerator.domain.model.Song
import com.simuel.musicgenerator.domain.model.SongGeneration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSongRepository : SongRepository {
    private val songs = mutableMapOf<String, Song>()
    private var generatedSong: Song? = null
    private var deletedSongIds = mutableListOf<String>()
    private var savedSongs = mutableListOf<Song>()
    
    // Test helper methods
    fun setSongById(id: String, song: Song) {
        songs[id] = song
    }
    
    fun setGeneratedSong(song: Song) {
        generatedSong = song
    }
    
    fun getDeletedSongIds() = deletedSongIds.toList()
    
    fun getSavedSongs() = savedSongs.toList()
    
    fun clear() {
        songs.clear()
        generatedSong = null
        deletedSongIds.clear()
        savedSongs.clear()
    }
    
    // Repository implementation
    override fun getAllSongs(): Flow<List<Song>> = flowOf(songs.values.toList())
    
    override suspend fun generateSong(request: SongGeneration): Song {
        return generatedSong ?: throw IllegalStateException("Generated song not set")
    }
    
    override suspend fun saveSong(song: Song) {
        songs[song.id] = song
        savedSongs.add(song)
    }
    
    override suspend fun getSongById(id: String): Song? = songs[id]
    
    override suspend fun deleteSong(id: String) {
        songs.remove(id)
        deletedSongIds.add(id)
    }
}