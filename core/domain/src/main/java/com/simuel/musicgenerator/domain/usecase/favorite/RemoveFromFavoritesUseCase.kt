package com.simuel.musicgenerator.domain.usecase.favorite

import com.simuel.musicgenerator.domain.repository.FavoriteRepository
import javax.inject.Inject

class RemoveFromFavoritesUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(songId: String) {
        require(songId.isNotBlank()) { "Song ID는 비어있을 수 없습니다" }
        favoriteRepository.removeFromFavorites(songId)
    }
}