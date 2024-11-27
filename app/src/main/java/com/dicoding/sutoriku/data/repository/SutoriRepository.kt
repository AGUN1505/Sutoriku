package com.dicoding.sutoriku.data.repository

import com.dicoding.sutoriku.data.Result
import com.dicoding.sutoriku.data.response.sutori.ListStoryItem
import com.dicoding.sutoriku.data.retrofit.ApiService

class SutoriRepository private constructor(private val apiService: ApiService) {
    suspend fun getSutori(): Result<List<ListStoryItem?>> {
        try {
            val response = apiService.getStories()
            if (response.error == true) {
                return Result.Error("Error: ${response.message}")
            } else {
                val story = response.listStory ?: emptyList()
                return Result.Success(story)
            }
        } catch (e: Exception) {
            return Result.Error(e.message.toString())
        }
    }

    companion object {

        @Volatile
        private var instance: SutoriRepository? = null

        fun getInstance(
            apiService: ApiService
        ): SutoriRepository =
            instance ?: synchronized(this) {
                instance ?: SutoriRepository(apiService)
            }.also { instance = it }
    }
}