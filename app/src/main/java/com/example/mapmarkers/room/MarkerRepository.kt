package com.example.mapmarkers.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class MarkerRepository(private val markerDao: MarkerDao) {

    val allMarkers: Flow<List<MarkerEntity>> = markerDao.getAll()

    @WorkerThread
    suspend fun insert(markerEntity: MarkerEntity) {
        markerDao.insert(markerEntity)
    }

    @WorkerThread
    suspend fun update(markerEntity: MarkerEntity) {
        markerDao.update(markerEntity)
    }

    @WorkerThread
    suspend fun delete(markerEntity: MarkerEntity) {
        markerDao.delete(markerEntity)
    }
}