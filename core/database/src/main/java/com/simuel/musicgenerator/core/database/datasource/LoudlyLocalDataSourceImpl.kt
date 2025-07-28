package com.simuel.musicgenerator.core.database.datasource

import com.simuel.musicgenerator.core.database.dao.FavoriteSongDao
import com.simuel.musicgenerator.core.database.dao.SongDao
import com.simuel.musicgenerator.core.database.entity.FavoriteSongEntity
import com.simuel.musicgenerator.core.database.entity.SongEntity
import com.simuel.musicgenerator.data.datasource.LoudlyLocalDataSource
import com.simuel.musicgenerator.data.model.FavoriteSong
import com.simuel.musicgenerator.data.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LoudlyLocalDataSourceImpl @Inject constructor(
    private val songDao: SongDao,
    private val favoriteSongDao: FavoriteSongDao
) : LoudlyLocalDataSource {

    override suspend fun saveSong(song: Song) {
        songDao.insertSong(
            SongEntity(
                id = song.id,
                title = song.title,
                duration = song.duration,
                musicFilePath = song.musicFilePath,
                waveFormFilePath = song.waveFormFilePath,
                createdAt = song.createdAt,
                bpm = song.bpm,
                musicKeyId = song.musicKeyId,
                musicKeyName = song.musicKeyName,
                musicKeyActive = song.musicKeyActive
            )
        )
    }

    override fun getAllSongs(): Flow<List<Song>> {
        return songDao.getAllSongs().map { entities -> 
            entities.map { entity ->
                Song(
                    id = entity.id,
                    title = entity.title,
                    duration = entity.duration,
                    musicFilePath = entity.musicFilePath,
                    waveFormFilePath = entity.waveFormFilePath,
                    createdAt = entity.createdAt,
                    bpm = entity.bpm,
                    musicKeyId = entity.musicKeyId,
                    musicKeyName = entity.musicKeyName,
                    musicKeyActive = entity.musicKeyActive
                )
            }
        }
    }
    
    override suspend fun deleteSong(id: String) {
        songDao.deleteSong(id)
    }
    
    override suspend fun addToFavorites(songId: String) {
        val favoriteSong = FavoriteSongEntity(songId = songId)
        favoriteSongDao.addFavorite(favoriteSong)
    }
    
    override suspend fun removeFromFavorites(songId: String) {
        favoriteSongDao.removeFavorite(songId)
    }
    
    override suspend fun isFavorite(songId: String): Boolean {
        return favoriteSongDao.isFavorite(songId)
    }
    
    override fun getAllFavorites(): Flow<List<FavoriteSong>> {
        return favoriteSongDao.getAllFavorites().map { entities ->
            entities.map { entity ->
                FavoriteSong(songId = entity.songId)
            }
        }
    }
}
