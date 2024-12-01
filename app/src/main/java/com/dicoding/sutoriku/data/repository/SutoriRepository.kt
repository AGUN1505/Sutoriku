package com.dicoding.sutoriku.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dicoding.sutoriku.data.database.*
import com.dicoding.sutoriku.data.response.sutori.ListStoryItem
import com.dicoding.sutoriku.data.retrofit.ApiService

class SutoriRepository private constructor(private val apiService: ApiService, private val sutoriDatabase: SutoriDatabase) {
    fun getSutori(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = SutoriRemoteMediator(sutoriDatabase, apiService),
            pagingSourceFactory = {
                sutoriDatabase.sutoriDao().getAllStory()
            }
        ).liveData
    }

    companion object {

        @Volatile
        private var instance: SutoriRepository? = null

        fun getInstance(
            sutoriDatabase: SutoriDatabase,
            apiService: ApiService
        ): SutoriRepository =
            instance ?: synchronized(this) {
                instance ?: SutoriRepository(apiService, sutoriDatabase)
            }.also { instance = it }
    }
}