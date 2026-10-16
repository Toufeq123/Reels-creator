package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReelProjectDao {
    @Query("SELECT * FROM reel_projects ORDER BY createdAt DESC")
    fun getAllReels(): Flow<List<ReelProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReel(project: ReelProjectEntity): Long

    @Delete
    suspend fun deleteReel(project: ReelProjectEntity)

    @Query("DELETE FROM reel_projects WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM reel_projects")
    suspend fun getCount(): Int
}
