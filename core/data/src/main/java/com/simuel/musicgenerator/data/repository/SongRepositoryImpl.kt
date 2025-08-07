package com.simuel.musicgenerator.data.repository

import com.simuel.musicgenerator.data.datasource.LoudlyLocalDataSource
import com.simuel.musicgenerator.data.datasource.LoudlyRemoteDataSource
import com.simuel.musicgenerator.data.model.SongDto
import com.simuel.musicgenerator.data.model.SongGenerationDto
import com.simuel.musicgenerator.domain.model.Song
import com.simuel.musicgenerator.domain.model.SongGeneration
import com.simuel.musicgenerator.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SongRepositoryImpl @Inject constructor(
    private val remoteDataSource: LoudlyRemoteDataSource,
    private val localDataSource: LoudlyLocalDataSource
) : SongRepository {

    override suspend fun generateSong(songGeneration: SongGeneration): Song {
        val request = SongGenerationDto(
            prompt = songGeneration.prompt,
            durationInSeconds = songGeneration.durationInSeconds,
            isTest = songGeneration.isTest,
            structureId = songGeneration.structureId
        )
        
        val songDto = remoteDataSource.generateSong(request)
        
        val song = Song(
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
        
        localDataSource.saveSong(songDto)
        
        return song
    }

    override fun getAllSongs(): Flow<List<Song>> {
        return localDataSource.getAllSongs().map { songDtos ->
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

    override suspend fun getSongById(id: String): Song? {
        return localDataSource.getSongById(id)?.let { songDto ->
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

    override suspend fun deleteSong(id: String) {
        localDataSource.deleteSong(id)
    }

    override suspend fun saveSong(song: Song) {
        val songDto = SongDto(
            id = song.id,
            title = song.title,
            duration = song.duration,
            musicFilePath = song.musicFilePath,
            waveFormFilePath = song.waveFormFilePath ?: "",
            createdAt = song.createdAt,
            bpm = song.bpm,
            musicKeyId = song.musicKeyId,
            musicKeyName = song.musicKeyName,
            musicKeyActive = song.musicKeyActive
        )
        localDataSource.saveSong(songDto)
    }
}