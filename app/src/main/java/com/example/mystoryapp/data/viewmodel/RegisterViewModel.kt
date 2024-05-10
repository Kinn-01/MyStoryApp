package com.example.mystoryapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.repository.UserRepository
import com.example.mystoryapp.data.retrofit.response.ErrorResponse
import com.example.mystoryapp.data.retrofit.response.LoginResponse
import com.example.mystoryapp.data.retrofit.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException


class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String>get() = _isError

    fun register(name: String, email: String, password: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val message = userRepository.register(name, email, password)
                _isLoading.postValue(false)
                val messageRegis = message.message
                _isError.value = messageRegis!!
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _isLoading.postValue(false)
                _isError.value = errorMessage ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

}