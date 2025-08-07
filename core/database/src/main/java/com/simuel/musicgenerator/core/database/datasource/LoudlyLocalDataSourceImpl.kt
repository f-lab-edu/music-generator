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
        songDao.insertSong(song.toEntity())
    }

    override fun getAllSongs(): Flow<List<SongDto>> {
        return songDao.getAllSongs().map { entities -> 
            entities.map { entity -> entity.toDto() }
        }
    }
    
    override suspend fun getSongById(id: String): SongDto? {
        return songDao.getSongById(id)?.toDto()
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
            entities.map { entity -> entity.toDto() }
        }
    }
    
    override fun getFavoriteSongsWithDetails(): Flow<List<SongDto>> {
        return favoriteSongDao.getAllFavorites().map { favoriteEntities ->
            favoriteEntities.mapNotNull { favoriteEntity ->
                songDao.getSongById(favoriteEntity.songId)?.toDto()
            }
        }
    }
    
    private fun SongEntity.toDto(): SongDto {
        return SongDto(
            id = id,
            title = title,
            duration = duration,
            musicFilePath = musicFilePath,
            waveFormFilePath = waveFormFilePath,
            createdAt = createdAt,
            bpm = bpm,
            musicKeyId = musicKeyId,
            musicKeyName = musicKeyName,
            musicKeyActive = musicKeyActive
        )
    }
    
    private fun SongDto.toEntity(): SongEntity {
        return SongEntity(
            id = id,
            title = title,
            duration = duration,
            musicFilePath = musicFilePath,
            waveFormFilePath = waveFormFilePath,
            createdAt = createdAt,
            bpm = bpm,
            musicKeyId = musicKeyId,
            musicKeyName = musicKeyName,
            musicKeyActive = musicKeyActive
        )
    }
    
    private fun FavoriteSongEntity.toDto(): FavoriteSongDto {
        return FavoriteSongDto(songId = songId)
    }
}
