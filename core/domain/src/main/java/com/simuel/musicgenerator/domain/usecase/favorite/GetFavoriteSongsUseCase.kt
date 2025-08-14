package com.simuel.musicgenerator.domain.usecase.favorite

import com.simuel.musicgenerator.domain.model.Song
import com.simuel.musicgenerator.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteSongsUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(): Flow<List<Song>> {
        return favoriteRepository.getFavoriteSongsWithDetails()
    }
}