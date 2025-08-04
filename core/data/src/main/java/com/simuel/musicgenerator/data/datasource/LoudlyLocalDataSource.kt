package com.simuel.musicgenerator.data.datasource

import com.simuel.musicgenerator.data.model.FavoriteSongDto
import com.simuel.musicgenerator.data.model.SongDto
import kotlinx.coroutines.flow.Flow

interface LoudlyLocalDataSource {
    
    suspend fun saveSong(song: SongDto)
    
    fun getAllSongs(): Flow<List<SongDto>>
    
    suspend fun getSongById(id: String): SongDto?
    
    suspend fun deleteSong(id: String)
    
    suspend fun addToFavorites(songId: String)
    
    suspend fun removeFromFavorites(songId: String)
    
    fun getAllFavorites(): Flow<List<FavoriteSongDto>>
    
    fun getFavoriteSongsWithDetails(): Flow<List<SongDto>>
}
