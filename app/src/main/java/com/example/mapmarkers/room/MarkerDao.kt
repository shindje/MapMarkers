package com.example.mapmarkers.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MarkerDao {

    @Query("SELECT * FROM MarkerEntity")
    fun getAll(): Flow<List<MarkerEntity>>

    @Insert
    suspend fun insert(markerEntity: MarkerEntity)

    @Update
    suspend fun update(markerEntity: MarkerEntity)

    @Delete
    suspend fun delete(markerEntity: MarkerEntity)
}