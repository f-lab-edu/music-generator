package com.simuel.musicgenerator.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simuel.musicgenerator.core.database.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SongDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: SongEntity)
    
    @Query("SELECT * FROM songs WHERE id = :id")
    suspend fun getSongById(id: String): SongEntity?
    
    @Query("SELECT * FROM songs ORDER BY createdAt DESC")
    fun getAllSongs(): Flow<List<SongEntity>>
    
    @Query("DELETE FROM songs WHERE id = :id")
    suspend fun deleteSong(id: String)
}