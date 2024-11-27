package com.dicoding.sutoriku.ui.login

import android.app.Application
import androidx.lifecycle.*
import com.dicoding.sutoriku.data.pref.UserPreference
import com.dicoding.sutoriku.data.repository.LoginRepository
import com.dicoding.sutoriku.di.Injection

class LoginViewModelFactory private constructor(
    private val application: Application,
    private val loginRepository: LoginRepository,
    private val userPreference: UserPreference
) : ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(application, loginRepository, userPreference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class:" + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: LoginViewModelFactory? = null
        fun getInstance(
            application: Application,
            userPreference: UserPreference
        ): LoginViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: LoginViewModelFactory(
                    application,
                    Injection.loginRepository(application),
                    userPreference
                )
            }.also { instance = it }
    }
}