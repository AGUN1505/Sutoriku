package com.dicoding.sutoriku.ui.userSetting

import android.content.Context
import androidx.lifecycle.*
import com.dicoding.sutoriku.data.pref.UserPreference
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class uSettingViewModel (private val userPreference: UserPreference) : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch {
            _userName.value = userPreference.getUserName()
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreference.clearUserData()
            delay(1000)
        }
    }
}