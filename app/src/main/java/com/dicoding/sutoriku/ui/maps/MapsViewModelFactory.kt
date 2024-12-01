package com.dicoding.sutoriku.ui.maps

import android.content.Context
import androidx.lifecycle.*
import com.dicoding.sutoriku.data.repository.SutoriLocationRepository
import com.dicoding.sutoriku.di.Injection

class MapsViewModelFactory private constructor(
    private val storyLocationRepository: SutoriLocationRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(storyLocationRepository) as T
        } else {
            throw IllegalArgumentException("Viewmodel class tidak diketahui: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: MapsViewModelFactory? = null
        fun getInstance(
            context: Context
        ): MapsViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MapsViewModelFactory(
                    Injection.sutoriLocationRepository(context)
                )
            }.also { instance = it }
    }
}