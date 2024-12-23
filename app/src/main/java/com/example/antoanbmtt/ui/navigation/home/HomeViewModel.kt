package com.example.antoanbmtt.ui.navigation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antoanbmtt.api.ApiResult
import com.example.antoanbmtt.api.resource.ResourceContent
import com.example.antoanbmtt.repository.ResourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val resourceRepository: ResourceRepository
) : ViewModel() {
    private val _homeUiState = MutableLiveData(HomeUiState())
    val homeUiState : LiveData<HomeUiState> get() = _homeUiState
    init{
        getResources()
    }
    private val _resourceContent = MutableLiveData(ResourceContent())
    val resourceContent : LiveData<ResourceContent> get() = _resourceContent
    fun pushResource(file : MultipartBody.Part){
        _homeUiState.value = _homeUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.postResource(file)){
                is ApiResult.Success -> _homeUiState.value = _homeUiState.value?.copy(resources = result.data, isLoading = false)
                is ApiResult.Failure -> _homeUiState.value = _homeUiState.value?.copy(errorMessage = result.message, isLoading = false)
            }
        }
    }
    fun getResources(){
        viewModelScope.launch {
            _homeUiState.value = _homeUiState.value?.copy(isLoading = true)
            when(val result = resourceRepository.getResources()){
                is ApiResult.Success -> _homeUiState.value = _homeUiState.value?.copy(resources = result.data, isLoading = false)
                is ApiResult.Failure -> _homeUiState.value = _homeUiState.value?.copy(errorMessage = result.message, isLoading = false)
            }
        }
    }
    fun messageShown(){
        _homeUiState.value = _homeUiState.value?.copy(errorMessage = null)
    }
    fun getResourceContent(uri : String){
        _homeUiState.value = _homeUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.getResourceContent(uri)){
                is ApiResult.Success -> {
                    _resourceContent.value = result.data
                    _homeUiState.value = _homeUiState.value?.copy(isLoading = false)
                }
                is ApiResult.Failure -> _homeUiState.value = _homeUiState.value?.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
    fun resetContent(){
        _resourceContent.value = ResourceContent()
    }
}