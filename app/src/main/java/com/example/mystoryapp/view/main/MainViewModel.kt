package com.example.mystoryapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.data.repository.UserRepository
import com.example.mystoryapp.data.preferences.UserModel
import com.example.mystoryapp.data.retrofit.response.ListStoryItem
import kotlinx.coroutines.launch
class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    val listStory: LiveData<PagingData<ListStoryItem>> = repository.getStory().cachedIn(viewModelScope)
    private val _Message = MutableLiveData<String>()
    val  message: LiveData<String> get() = _Message
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}