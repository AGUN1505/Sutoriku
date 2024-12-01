package com.dicoding.sutoriku.data.repository

import android.util.Log
import com.dicoding.sutoriku.data.Result
import com.dicoding.sutoriku.data.response.sutori.ListStoryItem
import com.dicoding.sutoriku.data.retrofit.ApiService

class SutoriLocationRepository private constructor(private val apiService: ApiService) {
    suspend fun getSutoriLocation(): Result<List<ListStoryItem?>> {
        try {
            val response = apiService.getSutoriLocation()
            if (response.error == true) {
                return Result.Error("Error: ${response.message}")
            } else {
                val storyLocation = response.listStory ?: emptyList()
                return Result.Success(storyLocation)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getStoryLocation: ${e.message.toString()}")
            return Result.Error(e.message.toString())
        }
    }

    companion object {
        private const val TAG = "StoryLocationRepository"

        @Volatile
        private var instance: SutoriLocationRepository? = null

        fun getInstance(apiService: ApiService): SutoriLocationRepository =
            instance ?: synchronized(this) {
                instance ?: SutoriLocationRepository(apiService)
            }.also { instance = it }
    }
}