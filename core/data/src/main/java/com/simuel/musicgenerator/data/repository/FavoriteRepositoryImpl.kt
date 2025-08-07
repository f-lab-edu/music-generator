package com.simuel.musicgenerator.data.repository

import com.simuel.musicgenerator.data.datasource.LoudlyLocalDataSource
import com.simuel.musicgenerator.domain.model.FavoriteSong
import com.simuel.musicgenerator.domain.model.Song
import com.simuel.musicgenerator.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class FavoriteRepositoryImpl @Inject constructor(
    private val localDataSource: LoudlyLocalDataSource
) : FavoriteRepository {

    override suspend fun addToFavorites(songId: String) {
        localDataSource.addToFavorites(songId)
    }

    override suspend fun removeFromFavorites(songId: String) {
        localDataSource.removeFromFavorites(songId)
    }


    override fun getFavoriteSongsWithDetails(): Flow<List<Song>> {
        return localDataSource.getFavoriteSongsWithDetails().map { songDtos ->
            songDtos.map { songDto ->
                Song(
                    id = songDto.id,
                    title = songDto.title,
                    duration = songDto.duration,
                    musicFilePath = songDto.musicFilePath,
                    waveFormFilePath = songDto.waveFormFilePath,
                    createdAt = songDto.createdAt,
                    bpm = songDto.bpm,
                    musicKeyId = songDto.musicKeyId,
                    musicKeyName = songDto.musicKeyName,
                    musicKeyActive = songDto.musicKeyActive
                )
            }
        }
    }
}