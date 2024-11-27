package com.dicoding.sutoriku.ui.register

import android.app.Application
import androidx.lifecycle.*
import com.dicoding.sutoriku.R
import com.dicoding.sutoriku.data.repository.RegisterRepository
import com.dicoding.sutoriku.data.response.register.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.HttpException

class RegisterViewModel (application: Application, private val registerRepository: RegisterRepository) : AndroidViewModel(application) {
    private val _successDialog = MutableLiveData<String>()
    val successDialog: LiveData<String> get() = _successDialog

    private val _errorDialog = MutableLiveData<String>()
    val errorDialog: LiveData<String> get() = _errorDialog

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun registerUser(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                registerRepository.registerUser(name, email, password)
                _isLoading.postValue(false)
                _successDialog.postValue(getApplication<Application>().getString(R.string.register_succes))
            } catch (e: HttpException) {
                _isLoading.postValue(false)
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
                val errorMessage =
                    errorBody.message
                        ?: getApplication<Application>().getString(R.string.register_error)
                _errorDialog.postValue(errorMessage)
            } catch (e: Exception) {
                _isLoading.postValue(false)
                _errorDialog.postValue(
                    R.string.register_error_with_error_messege.toString()
                )
            }
        }
    }
}