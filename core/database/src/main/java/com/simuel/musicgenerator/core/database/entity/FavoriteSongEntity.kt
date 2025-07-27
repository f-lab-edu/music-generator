package com.simuel.musicgenerator.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_songs",
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FavoriteSongEntity(
    @PrimaryKey
    val songId: String,
    val addedAt: Long = System.currentTimeMillis()
)