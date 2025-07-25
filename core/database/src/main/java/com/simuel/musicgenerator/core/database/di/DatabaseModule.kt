package com.simuel.musicgenerator.core.database.di

import android.content.Context
import androidx.room.Room
import com.simuel.musicgenerator.core.database.db.MusicGeneratorDatabase
import com.simuel.musicgenerator.core.database.dao.FavoriteSongDao
import com.simuel.musicgenerator.core.database.dao.SongDao
import com.simuel.musicgenerator.core.database.datasource.LoudlyLocalDataSourceImpl
import com.simuel.musicgenerator.data.datasource.LoudlyLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    
    @Provides
    fun provideMusicGeneratorDatabase(
        @ApplicationContext context: Context
    ): MusicGeneratorDatabase {
        return Room.databaseBuilder(
            context,
            MusicGeneratorDatabase::class.java,
            "music_generator_database"
        ).build()
    }
    
    @Provides
    fun provideSongDao(database: MusicGeneratorDatabase): SongDao {
        return database.songDao()
    }
    
    @Provides
    fun provideFavoriteSongDao(database: MusicGeneratorDatabase): FavoriteSongDao {
        return database.favoriteSongDao()
    }

    @Provides
    fun provideLoudlyLocalDataSource(
        songDao: SongDao,
        favoriteSongDao: FavoriteSongDao
    ): LoudlyLocalDataSource {
        return LoudlyLocalDataSourceImpl(songDao, favoriteSongDao)
    }
}
