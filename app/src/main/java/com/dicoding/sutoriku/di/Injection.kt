package com.dicoding.sutoriku.di

import android.content.Context
import com.dicoding.sutoriku.data.pref.*
import com.dicoding.sutoriku.data.repository.*
import com.dicoding.sutoriku.data.retrofit.ApiConfig
import kotlinx.coroutines.runBlocking

object Injection {

    fun registerRepository(context: Context): RegisterRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return RegisterRepository.getInstance(apiService)
    }

    fun loginRepository(context: Context): LoginRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return LoginRepository.getInstance(apiService)
    }

    fun addSutoriRepository(context: Context): AddSutoriRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return AddSutoriRepository.getInstance(apiService)
    }

    fun sutoriRepository(context: Context): SutoriRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return SutoriRepository.getInstance(apiService)
    }

    fun detailSutoriRepository(context: Context): DetailSutoriRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return DetailSutoriRepository.getInstance(apiService)
    }

    fun userPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context.dataStore)
    }
}