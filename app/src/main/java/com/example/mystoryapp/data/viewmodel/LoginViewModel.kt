package com.example.mystoryapp.data.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.preferences.UserModel
import com.example.mystoryapp.data.repository.UserRepository
import com.example.mystoryapp.data.retrofit.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val UserRepository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String> get() = _isError

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = UserRepository.login(email, password)
                setAuth(
                    UserModel(
                        response.loginResult.userId,
                        response.loginResult.name,
                        email,
                        response.loginResult.token,
                        true
                    )
                )
                _isLoading.postValue(false)
                _loginResponse.postValue(response)
                Log.d(TAG, "isSuccess ${response.message}")
            } catch (e: HttpException) {
//                _isLoading.value = false
//                val jsonInString = e.response()?.errorBody()?.string()
//                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
//                val errorMessage = errorBody.message
//                _isError.value = errorMessage ?: "Unknown error"
                _isLoading.postValue(false)
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                _isLoading.postValue(false)
                _loginResponse.postValue(errorBody)
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }

    private fun setAuth(userModel: UserModel) {
        viewModelScope.launch {
            UserRepository.setAuth(userModel)
        }
    }

}