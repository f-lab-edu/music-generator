package com.simuel.musicgenerator.domain.repository

import com.simuel.musicgenerator.domain.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeFavoriteRepository : FavoriteRepository {
    private val favorites = mutableSetOf<String>()
    private var favoriteSongs = emptyList<Song>()
    var addCallCount = 0
    var removeCallCount = 0
    var lastAddedSongId: String? = null
    var lastRemovedSongId: String? = null
    
    // Test helper methods
    fun setFavoriteSongs(songs: List<Song>) {
        this.favoriteSongs = songs
        favorites.clear()
        songs.forEach { favorites.add(it.id) }
    }
    
    fun isFavorite(songId: String): Boolean = favorites.contains(songId)
    
    fun clear() {
        favorites.clear()
        favoriteSongs = emptyList()
        addCallCount = 0
        removeCallCount = 0
        lastAddedSongId = null
        lastRemovedSongId = null
    }
    
    // Repository implementation
    override suspend fun addToFavorites(songId: String) {
        addCallCount++
        lastAddedSongId = songId
        favorites.add(songId)
    }
    
    override suspend fun removeFromFavorites(songId: String) {
        removeCallCount++
        lastRemovedSongId = songId
        favorites.remove(songId)
    }
    
    override fun getFavoriteSongsWithDetails(): Flow<List<Song>> = flowOf(favoriteSongs)
}