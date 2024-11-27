package com.dicoding.sutoriku.ui.register

import android.app.Application
import androidx.lifecycle.*
import com.dicoding.sutoriku.data.repository.RegisterRepository
import com.dicoding.sutoriku.di.Injection

class RegisterViewModelFactory private constructor(
    private val application: Application,
    private val registerRepository: RegisterRepository
) : ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(application, registerRepository) as T
        }
        throw IllegalArgumentException("viewmodel class tidak ditemukan" + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: RegisterViewModelFactory? = null

        fun getInstance(application: Application): RegisterViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: RegisterViewModelFactory(
                    application,
                    Injection.registerRepository(application)
                )
            }.also { instance = it }
    }

}