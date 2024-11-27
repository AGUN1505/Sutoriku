package com.dicoding.sutoriku.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.sutoriku.data.Result
import com.dicoding.sutoriku.data.repository.SutoriRepository
import com.dicoding.sutoriku.data.response.sutori.ListStoryItem
import kotlinx.coroutines.launch

class HomeViewModel (private val sutoriRepository: SutoriRepository): ViewModel() {
    private val _story = MutableLiveData<Result<List<ListStoryItem?>>>()
    val story: LiveData<Result<List<ListStoryItem?>>>
        get() = _story

    fun findStory() {
        viewModelScope.launch {
            try {
                val result = sutoriRepository.getSutori()
                _story.value = result
            } catch (e: Exception) {
                _story.value = Result.Error(e.message.toString())
                Log.e("HomeViewModel", "Error: ${e.message.toString()}")
            }
        }
    }
}