package com.dicoding.sutoriku.ui.detail

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.sutoriku.data.Result
import com.dicoding.sutoriku.data.repository.DetailSutoriRepository
import com.dicoding.sutoriku.data.response.detail.Story
import kotlinx.coroutines.launch

class DetailViewModel (private val detailSutoriRepository: DetailSutoriRepository) : ViewModel() {
    private val _detailStory = MutableLiveData<Result<Story?>>()
    val storyDetail: LiveData<Result<Story?>> get() = _detailStory

    fun detailStory(storyId: String) {
        viewModelScope.launch {
            try {
                val result = detailSutoriRepository.detailSutori(storyId)
                _detailStory.value = result
            } catch (e: Exception) {
                _detailStory.value = Result.Error(e.message.toString())
                Log.e("DetailViewModel", "Error: ${e.message.toString()}")
            }
        }
    }
}