package com.dicoding.sutoriku.ui.maps

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.sutoriku.data.Result
import com.dicoding.sutoriku.data.repository.SutoriLocationRepository
import com.dicoding.sutoriku.data.response.sutori.ListStoryItem
import kotlinx.coroutines.launch

class MapsViewModel (private val sutoriLocationRepository: SutoriLocationRepository) : ViewModel() {
    private val _location = MutableLiveData<Result<List<ListStoryItem?>>>()
    val location: LiveData<Result<List<ListStoryItem?>>> get() = _location

    fun getStoryLocation() {
        viewModelScope.launch {
            try {
                val result = sutoriLocationRepository.getSutoriLocation()
                _location.value = result
                Log.d("MapsViewModel", "getStoryLocation: $result")
            } catch (e: Exception) {
                _location.value = Result.Error(e.message.toString())
                Log.e("MapsViewModel", "Error: &{e.message}")
            }
        }
    }
}