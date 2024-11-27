package com.dicoding.sutoriku.ui.userSetting

import android.content.Context
import androidx.lifecycle.*
import com.dicoding.sutoriku.data.pref.UserPreference
import com.dicoding.sutoriku.di.Injection

class uSettingViewModelFactory (
    private val userPreference: UserPreference
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(uSettingViewModel::class.java)) {
            return uSettingViewModel(userPreference) as T
        }
        throw IllegalArgumentException("viewmodel class tidak ditemukan" + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: uSettingViewModelFactory? = null

        fun getInstance(context: Context)
                : uSettingViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: uSettingViewModelFactory(
                    Injection.userPreference(context)
                )
            }.also { instance = it }
    }
}