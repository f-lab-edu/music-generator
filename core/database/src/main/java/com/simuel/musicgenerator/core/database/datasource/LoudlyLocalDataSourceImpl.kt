package com.simuel.musicgenerator.core.database.datasource

import com.simuel.musicgenerator.core.database.dao.FavoriteSongDao
import com.simuel.musicgenerator.core.database.dao.SongDao
import com.simuel.musicgenerator.core.database.entity.FavoriteSongEntity
import com.simuel.musicgenerator.core.database.entity.SongEntity
import com.simuel.musicgenerator.data.datasource.LoudlyLocalDataSource
import com.simuel.musicgenerator.data.model.FavoriteSongDto
import com.simuel.musicgenerator.data.model.SongDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LoudlyLocalDataSourceImpl @Inject constructor(
    private val songDao: SongDao,
    private val favoriteSongDao: FavoriteSongDao
) : LoudlyLocalDataSource {

    override suspend fun saveSong(song: SongDto) {
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

    override fun getAllSongs(): Flow<List<SongDto>> {
        return songDao.getAllSongs().map { entities -> 
            entities.map { entity ->
                SongDto(
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
    
    override suspend fun getSongById(id: String): SongDto? {
        return songDao.getSongById(id)?.let { entity ->
            SongDto(
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
    
    override fun getAllFavorites(): Flow<List<FavoriteSongDto>> {
        return favoriteSongDao.getAllFavorites().map { entities ->
            entities.map { entity ->
                FavoriteSongDto(songId = entity.songId)
            }
        }
    }
    
    override fun getFavoriteSongsWithDetails(): Flow<List<SongDto>> {
        return favoriteSongDao.getAllFavorites().map { favoriteEntities ->
            favoriteEntities.mapNotNull { favoriteEntity ->
                songDao.getSongById(favoriteEntity.songId)?.let { songEntity ->
                    SongDto(
                        id = songEntity.id,
                        title = songEntity.title,
                        duration = songEntity.duration,
                        musicFilePath = songEntity.musicFilePath,
                        waveFormFilePath = songEntity.waveFormFilePath,
                        createdAt = songEntity.createdAt,
                        bpm = songEntity.bpm,
                        musicKeyId = songEntity.musicKeyId,
                        musicKeyName = songEntity.musicKeyName,
                        musicKeyActive = songEntity.musicKeyActive
                    )
                }
            }
        }
    }
}
