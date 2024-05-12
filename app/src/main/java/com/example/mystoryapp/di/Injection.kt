package com.example.mystoryapp.di

import android.content.Context
import com.example.mystoryapp.data.repository.UserRepository
import com.example.mystoryapp.data.preferences.UserPreference
import com.example.mystoryapp.data.preferences.dataStore
import com.example.mystoryapp.data.retrofit.api.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref, apiService)
    }
}