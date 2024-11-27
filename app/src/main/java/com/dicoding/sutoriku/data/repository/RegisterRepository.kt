package com.dicoding.sutoriku.data.repository

import com.dicoding.sutoriku.data.response.register.RegisterResponse
import com.dicoding.sutoriku.data.retrofit.ApiService

class RegisterRepository (private val apiService: ApiService) {
    suspend fun registerUser(username: String, email: String, password: String): RegisterResponse {
        return apiService.registerUser(username, email, password)
    }

    companion object {

        @Volatile
        private var instance: RegisterRepository? = null

        fun getInstance(
            apiService: ApiService
        ): RegisterRepository =
            instance ?: synchronized(this) {
                instance ?: RegisterRepository(apiService)
            }.also { instance = it }
    }
}