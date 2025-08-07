package com.simuel.musicgenerator.domain.repository

import com.simuel.musicgenerator.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun addToFavorites(songId: String)
    suspend fun removeFromFavorites(songId: String)
    fun getFavoriteSongsWithDetails(): Flow<List<Song>>
}