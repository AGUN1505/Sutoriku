package com.dicoding.sutoriku.ui.home

import android.content.Context
import androidx.lifecycle.*
import com.dicoding.sutoriku.data.repository.SutoriRepository
import com.dicoding.sutoriku.di.Injection

class HomeViewModelFactory (private val sutoriRepository: SutoriRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(sutoriRepository) as T
        }
        throw IllegalArgumentException("viewmodel class tidak ditemukan" + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: HomeViewModelFactory? = null

        fun getInstance(
            context: Context
        ): HomeViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: HomeViewModelFactory(
                    Injection.sutoriRepository(context)
                )
            }.also { instance = it }
    }
}