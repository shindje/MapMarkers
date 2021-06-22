package com.example.mapmarkers

import androidx.lifecycle.*
import com.example.mapmarkers.room.MarkerEntity
import com.example.mapmarkers.room.MarkerRepository
import kotlinx.coroutines.launch

class MapsActivityViewModel(private val repository: MarkerRepository) : ViewModel() {
    val allMarkers: LiveData<List<MarkerEntity>> = repository.allMarkers.asLiveData()

    fun insert(markerEntity: MarkerEntity) = viewModelScope.launch {
        repository.insert(markerEntity)
    }

    fun update(markerEntity: MarkerEntity) = viewModelScope.launch {
        repository.update(markerEntity)
    }

    fun delete(markerEntity: MarkerEntity) = viewModelScope.launch {
        repository.delete(markerEntity)
    }
}

class MapsActivityViewModelFactory(private val repository: MarkerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}