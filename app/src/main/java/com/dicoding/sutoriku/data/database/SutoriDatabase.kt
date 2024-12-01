package com.dicoding.sutoriku.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.sutoriku.data.response.sutori.ListStoryItem


@Database(
    entities = [ListStoryItem::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class SutoriDatabase : RoomDatabase() {
    abstract fun sutoriDao(): SutoriDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var instance: SutoriDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): SutoriDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    SutoriDatabase::class.java, "sutori_database"
                )
                    .fallbackToDestructiveMigrationFrom()
                    .build()
                    .also { instance = it }
            }
        }
    }
}