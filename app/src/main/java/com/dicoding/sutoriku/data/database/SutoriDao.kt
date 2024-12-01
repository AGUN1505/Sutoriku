package com.dicoding.sutoriku.data.database

import androidx.paging.PagingSource
import androidx.room.*
import com.dicoding.sutoriku.data.response.sutori.ListStoryItem

@Dao
interface SutoriDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStoryItem>)

    @Query("SELECT * FROM sutori")
    fun getAllStory(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM sutori")
    suspend fun deleteAll()
}