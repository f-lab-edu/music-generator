package com.simuel.musicgenerator.data.datasource

import com.simuel.musicgenerator.data.model.FavoriteSong
import com.simuel.musicgenerator.data.model.Song
import kotlinx.coroutines.flow.Flow

interface LoudlyLocalDataSource {
    
    suspend fun saveSong(song: Song)
    
    fun getAllSongs(): Flow<List<Song>>
    
    suspend fun deleteSong(id: String)
    
    suspend fun addToFavorites(songId: String)
    
    suspend fun removeFromFavorites(songId: String)
    
    suspend fun isFavorite(songId: String): Boolean
    
    fun getAllFavorites(): Flow<List<FavoriteSong>>
}
