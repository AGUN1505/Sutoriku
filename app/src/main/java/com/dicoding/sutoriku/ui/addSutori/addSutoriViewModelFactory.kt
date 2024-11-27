package com.dicoding.sutoriku.ui.addSutori

import android.content.Context
import androidx.lifecycle.*
import com.dicoding.sutoriku.data.repository.AddSutoriRepository
import com.dicoding.sutoriku.di.Injection

class addSutoriViewModelFactory (
    private val addStoryRepository: AddSutoriRepository
) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(addSutoriViewModel::class.java)) {
            return addSutoriViewModel(addStoryRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: addSutoriViewModelFactory? = null

        fun getInstance(context: Context): addSutoriViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: addSutoriViewModelFactory(
                    Injection.addSutoriRepository(context)
                )
            }.also { instance = it }
    }
}