package com.dicoding.sutoriku.data.repository

import com.dicoding.sutoriku.data.Result
import com.dicoding.sutoriku.data.response.detail.Story
import com.dicoding.sutoriku.data.retrofit.ApiService

class DetailSutoriRepository private constructor(
    private val apiService: ApiService
) {
    suspend fun detailSutori(storyId: String): Result<Story?> {
        try {
            val response = apiService.getDetailStories(storyId)
            if (response.error == true) {
                return Result.Error("Error: ${response.message}")
            } else {
                val storyDetail = response.story
                return Result.Success(storyDetail)
            }
        } catch (e: Exception) {
            return Result.Error(e.message.toString())
        }
    }

    companion object {

        @Volatile
        private var instance: DetailSutoriRepository? = null

        fun getInstance(
            apiService: ApiService
        ): DetailSutoriRepository =
            instance ?: synchronized(this) {
                instance ?: DetailSutoriRepository(apiService)
            }.also { instance = it }
    }
}