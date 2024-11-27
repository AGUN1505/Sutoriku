package com.dicoding.sutoriku.data.repository

import com.dicoding.sutoriku.data.response.addsutori.AddSutoriResponse
import com.dicoding.sutoriku.data.retrofit.ApiService
import okhttp3.*

class AddSutoriRepository (private val apiService: ApiService) {
    suspend fun addSutori(file: MultipartBody.Part, description: RequestBody): AddSutoriResponse {
        return apiService.addStutori(file, description)

    }

    companion object {
        @Volatile
        private var instance: AddSutoriRepository? = null

        fun getInstance(
            apiService: ApiService
        ): AddSutoriRepository =
            instance ?: synchronized(this) {
                instance ?: AddSutoriRepository(apiService)
            }.also { instance = it }
    }
}