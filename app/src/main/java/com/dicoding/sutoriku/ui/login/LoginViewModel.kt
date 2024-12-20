package com.dicoding.sutoriku.ui.login

import android.app.Application
import androidx.lifecycle.*
import com.dicoding.sutoriku.R
import com.dicoding.sutoriku.data.pref.UserPreference
import com.dicoding.sutoriku.data.repository.LoginRepository
import com.dicoding.sutoriku.data.response.login.LoginResponse
import com.dicoding.sutoriku.utils.IdlingResource
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.HttpException

class LoginViewModel(
    application: Application,
    private val loginRepository: LoginRepository,
    private val userPreference: UserPreference
) : AndroidViewModel(application) {
    private val _successDialog = MutableLiveData<String>()
    val successDialog: LiveData<String> get() = _successDialog

    private val _errorDialog = MutableLiveData<String>()
    val errorDialog: LiveData<String> get() = _errorDialog

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loginUser(email: String, password: String) {
        _isLoading.value = true
        IdlingResource.increment()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = loginRepository.login(email, password)
                val loginResult = response.loginResult
                val userId = loginResult?.userId
                val username = loginResult?.name
                val userToken = loginResult?.token
                _isLoading.postValue(false)
                _successDialog.postValue(getApplication<Application>().getString(R.string.dialog_login_succes))
                userToken?.let { token ->
                    userId?.let { id ->
                        username?.let { name ->
                            saveUserData(id, name, token)
                        }
                    }
                }
            } catch (e: HttpException) {
                _isLoading.postValue(false)
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                    ?: getApplication<Application>().getString(R.string.message_login_failed)
                _errorDialog.postValue(errorMessage)
            } catch (e: Exception) {
                _isLoading.postValue(false)
                _errorDialog.postValue(
                    getApplication<Application>().getString(R.string.login_failed_dialog)
                )
            } finally {
                IdlingResource.decrement()
            }
        }
    }

    private fun saveUserData(userId: String, username: String, userToken: String) {
        viewModelScope.launch {
            userPreference.saveUserData(token = userToken, id = userId, name = username)
        }
    }
}



