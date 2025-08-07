package com.simuel.musicgenerator.data.di

import com.simuel.musicgenerator.data.repository.AccountRepositoryImpl
import com.simuel.musicgenerator.data.repository.FavoriteRepositoryImpl
import com.simuel.musicgenerator.data.repository.PromptRepositoryImpl
import com.simuel.musicgenerator.data.repository.SongRepositoryImpl
import com.simuel.musicgenerator.domain.repository.AccountRepository
import com.simuel.musicgenerator.domain.repository.FavoriteRepository
import com.simuel.musicgenerator.domain.repository.PromptRepository
import com.simuel.musicgenerator.domain.repository.SongRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindSongRepository(impl: SongRepositoryImpl): SongRepository

    @Binds
    abstract fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    abstract fun bindPromptRepository(impl: PromptRepositoryImpl): PromptRepository
}
