package com.simuel.musicgenerator.domain.repository

import com.simuel.musicgenerator.domain.model.Song
import com.simuel.musicgenerator.domain.model.SongGeneration
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    suspend fun generateSong(request: SongGeneration): Song
    suspend fun saveSong(song: Song)
    fun getAllSongs(): Flow<List<Song>>
    suspend fun getSongById(id: String): Song?
    suspend fun deleteSong(id: String)
}