package com.dicoding.sutoriku.data.repository

import com.dicoding.sutoriku.data.response.login.LoginResponse
import com.dicoding.sutoriku.data.retrofit.ApiService

class LoginRepository (private val apiService: ApiService) {
    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.loginUser(email, password)
    }

    companion object {
        @Volatile
        private var instance: LoginRepository? = null

        fun getInstance(
            apiService: ApiService
        ): LoginRepository =
            instance ?: synchronized(this) {
                instance ?: LoginRepository(apiService)
            }.also { instance = it }
    }
}