package com.dicoding.sutoriku.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val userToken = stringPreferencesKey("user_token")
    private val userId = stringPreferencesKey("user_id")
    private val userName = stringPreferencesKey("user_name")

    suspend fun getUserToken(): String {
        val preferences = dataStore.data.first()
        return preferences[userToken] ?: ""
    }

    suspend fun getUserName(): String {
        val preferences = dataStore.data.first()
        return preferences[userName] ?: ""
    }

    suspend fun saveUserData(token: String, id: String, name: String) {
        clearUserData()
        dataStore.edit { preferences ->
            preferences[userToken] = token
            preferences[userId] = id
            preferences[userName] = name
        }
    }

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}