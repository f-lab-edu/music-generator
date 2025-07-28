package com.simuel.musicgenerator.core.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.simuel.musicgenerator.core.database.dao.FavoriteSongDao
import com.simuel.musicgenerator.core.database.dao.SongDao
import com.simuel.musicgenerator.core.database.entity.FavoriteSongEntity
import com.simuel.musicgenerator.core.database.entity.SongEntity

@Database(
    entities = [
        SongEntity::class,
        FavoriteSongEntity::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class MusicGeneratorDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao
    abstract fun favoriteSongDao(): FavoriteSongDao
}
