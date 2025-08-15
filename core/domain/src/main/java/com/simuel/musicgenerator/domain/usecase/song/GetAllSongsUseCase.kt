package com.simuel.musicgenerator.domain.usecase.song

import com.simuel.musicgenerator.domain.model.Song
import com.simuel.musicgenerator.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSongsUseCase @Inject constructor(
    private val songRepository: SongRepository
) {
    suspend operator fun invoke(): Flow<List<Song>> {
        return songRepository.getAllSongs()
    }
}