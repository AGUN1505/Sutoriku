package com.dicoding.sutoriku.ui.detail

import android.app.Application
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.*
import com.dicoding.sutoriku.R
import com.dicoding.sutoriku.data.Result
import com.dicoding.sutoriku.data.repository.DetailSutoriRepository
import com.dicoding.sutoriku.data.response.detail.Story
import kotlinx.coroutines.launch
import java.util.Locale

class DetailViewModel (application: Application, private val detailSutoriRepository: DetailSutoriRepository) : AndroidViewModel(application) {
    private val _detailStory = MutableLiveData<Result<Story?>>()
    val storyDetail: LiveData<Result<Story?>> get() = _detailStory

    private val _locationName = MutableLiveData<String>()
    val locationName: LiveData<String> get() = _locationName

    @Suppress("DEPRECATION")
    private fun getLocationName(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val geocoder = Geocoder(getApplication(), Locale.getDefault())
                val address = geocoder.getFromLocation(lat, lon, 1)
                if (!address.isNullOrEmpty()) {
                    val location = address[0]
                    val province = location.adminArea
                    _locationName.value = province
                        ?: getApplication<Application>().getString(R.string.no_location_story)
                } else {
                    _locationName.value =
                        getApplication<Application>().getString(R.string.no_location_story)
                }
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error: ${e.message.toString()}")
            }
        }
    }

    fun detailStory(storyId: String) {
        viewModelScope.launch {
            try {
                val result = detailSutoriRepository.detailSutori(storyId)
                _detailStory.value = result
                if (result is Result.Success) {
                    val story = result.data
                    if (story?.lat != null && story.lon != null) {
                        getLocationName(
                            story.lat.toString().toDouble(),
                            story.lon.toString().toDouble()
                        )
                    }
                }
            } catch (e: Exception) {
                _detailStory.value = Result.Error(e.message.toString())
                Log.e("DetailViewModel", "Error: ${e.message.toString()}")
            }
        }
    }
}