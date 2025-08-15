package com.simuel.musicgenerator.domain.usecase.favorite

import com.simuel.musicgenerator.domain.repository.FavoriteRepository
import com.simuel.musicgenerator.domain.repository.SongRepository
import javax.inject.Inject

class AddToFavoritesUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val songRepository: SongRepository
) {
    suspend operator fun invoke(songId: String) {
        require(songId.isNotBlank()) { "Song ID는 비어있을 수 없습니다" }
        
        val song = songRepository.getSongById(songId)
        requireNotNull(song) { "즐겨찾기에 추가할 음악을 찾을 수 없습니다: $songId" }
        
        favoriteRepository.addToFavorites(songId)
    }
}