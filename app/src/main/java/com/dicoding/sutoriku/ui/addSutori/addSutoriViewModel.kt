package com.dicoding.sutoriku.ui.addSutori

import android.net.Uri
import androidx.lifecycle.*
import com.dicoding.sutoriku.data.repository.AddSutoriRepository
import kotlinx.coroutines.launch
import okhttp3.*

class addSutoriViewModel (private val addStoryRepository: AddSutoriRepository) : ViewModel() {

    private val _selectUriImage = MutableLiveData<Uri?>()
    val selectUriImage: LiveData<Uri?> = _selectUriImage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _snackBar = MutableLiveData<String?>()
    val snackBar: MutableLiveData<String?> get() = _snackBar

    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSuccess: LiveData<Boolean> get() = _uploadSuccess

    fun setSelectImageUri(uri: Uri?) {
        _selectUriImage.value = uri
    }

    fun uploadImage(photo: MultipartBody.Part, description: RequestBody
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = addStoryRepository.addSutori(photo, description)
                _snackBar.value = response.message
                _uploadSuccess.value = true
            } catch (e: Exception) {
                _snackBar.value = e.message
                _uploadSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}