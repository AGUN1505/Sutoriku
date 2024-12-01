package com.dicoding.sutoriku.ui.detail

import android.app.Application
import androidx.lifecycle.*
import com.dicoding.sutoriku.data.repository.DetailSutoriRepository

class DetailViewModelFactory (private val application: Application, private val detailSutoriRepository: DetailSutoriRepository): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(application,detailSutoriRepository) as T
        }
        throw IllegalArgumentException("viewmodel class tidak ditemukan" + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: DetailViewModelFactory? = null

        fun getInstance(
            application: Application,
            detailSutoriRepository: DetailSutoriRepository
        ): DetailViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DetailViewModelFactory(
                    application,detailSutoriRepository
                )
            }.also { instance = it }
    }
}