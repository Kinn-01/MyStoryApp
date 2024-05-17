package com.example.mystoryapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.repository.UserRepository
import com.example.mystoryapp.data.preferences.UserModel
import com.example.mystoryapp.data.retrofit.response.ErrorResponse
import com.example.mystoryapp.data.retrofit.response.ListStoryItem
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> get() = _listStory

    private val _Message = MutableLiveData<String>()
    val  message: LiveData<String> get() = _Message
    suspend fun getAllStory() {
        try {
            val tokenStory = repository.getSession().first().token
            val story = repository.getStory("Bearer $tokenStory")
            val message = story.message

            _isLoading.value = false
            _listStory.value = story.listStory
            _Message.value = message!!
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message

            _isLoading.value = false
            _Message.value = errorMessage ?: "Unknown error"
        }
    }
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}