package com.dicoding.sutoriku.ui.detail

import android.content.Context
import androidx.lifecycle.*
import com.dicoding.sutoriku.data.repository.DetailSutoriRepository
import com.dicoding.sutoriku.di.Injection

class DetailViewModelFactory (private val detailSutoriRepository: DetailSutoriRepository): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(detailSutoriRepository) as T
        }
        throw IllegalArgumentException("viewmodel class tidak ditemukan" + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: DetailViewModelFactory? = null

        fun getInstance(
            context: Context,
            detailSutoriRepository: DetailSutoriRepository
        ): DetailViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DetailViewModelFactory(
                    Injection.detailSutoriRepository(context)
                )
            }.also { instance = it }
    }
}