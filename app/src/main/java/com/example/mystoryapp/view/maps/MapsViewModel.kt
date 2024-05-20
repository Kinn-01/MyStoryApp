package com.example.mystoryapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.preferences.UserModel
import com.example.mystoryapp.data.repository.UserRepository
import com.example.mystoryapp.data.retrofit.response.ErrorResponse
import com.example.mystoryapp.data.retrofit.response.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> get() = _user

    private val _mapsResponse = MutableLiveData<StoryResponse>()
    val mapsResponse: LiveData<StoryResponse> = _mapsResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                _user.value = user
            }
        }
    }

    fun getAllStoryLocation(token: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = repository.getStoryLocation(token)
                _isLoading.postValue(false)
                _mapsResponse.postValue(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
                val errorMessage = errorBody.message
                _isLoading.postValue(false)
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}