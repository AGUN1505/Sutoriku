package com.dicoding.sutoriku.ui.camera

import android.net.Uri
import androidx.lifecycle.*

class CameraViewModel : ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: MutableLiveData<Uri?>
        get() = _imageUri

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }
}