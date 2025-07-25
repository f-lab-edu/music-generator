package com.simuel.musicgenerator.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val duration: Int,
    val musicFilePath: String,
    val waveFormFilePath: String,
    val createdAt: String,
    val bpm: Int? = null,
    val musicKeyId: Int? = null,
    val musicKeyName: String? = null,
    val musicKeyActive: Boolean? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SongEntity
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}