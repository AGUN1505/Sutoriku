package com.dicoding.sutoriku.ui.home

import androidx.lifecycle.*
import androidx.paging.*
import com.dicoding.sutoriku.data.repository.SutoriRepository
import com.dicoding.sutoriku.data.response.sutori.ListStoryItem

class HomeViewModel (sutoriRepository: SutoriRepository): ViewModel() {
    val story: LiveData<PagingData<ListStoryItem>> = sutoriRepository.getSutori().cachedIn(viewModelScope)
}