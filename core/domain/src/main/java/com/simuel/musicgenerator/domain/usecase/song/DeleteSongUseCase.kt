package com.simuel.musicgenerator.domain.usecase.song

import com.simuel.musicgenerator.domain.repository.SongRepository
import javax.inject.Inject

class DeleteSongUseCase @Inject constructor(
    private val songRepository: SongRepository
) {
    suspend operator fun invoke(songId: String) {
        require(songId.isNotBlank()) { "Song ID는 비어있을 수 없습니다" }
        
        val existingSong = songRepository.getSongById(songId)
        requireNotNull(existingSong) { "삭제하려는 음악을 찾을 수 없습니다: $songId" }
        
        songRepository.deleteSong(songId)
    }
}