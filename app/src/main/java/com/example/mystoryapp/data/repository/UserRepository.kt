package com.example.mystoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mystoryapp.data.adapter.StoryPagingSource
import com.example.mystoryapp.data.preferences.UserModel
import com.example.mystoryapp.data.preferences.UserPreference
import com.example.mystoryapp.data.retrofit.api.ApiService
import com.example.mystoryapp.data.retrofit.response.ListStoryItem
import com.example.mystoryapp.data.retrofit.response.LoginResponse
import com.example.mystoryapp.data.retrofit.response.RegisterResponse
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(private val userPreference: UserPreference, private val apiService: ApiService) {
    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }
    suspend fun register(name: String, email: String, password: String): RegisterResponse{
        return apiService.register(name, email, password)
    }
    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,        // Mengatur ukuran halaman menjadi 20
                initialLoadSize = 20  // Mengatur ukuran load awal menjadi 20
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, userPreference)
            }
        ).liveData
    }
    suspend fun getStoryLocation(token: String) = apiService.getStoriesWithLocation(token)
    suspend fun setAuth(user: UserModel) = userPreference.saveSession(user)
    suspend fun logout() {
        userPreference.logout()
    }
    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}