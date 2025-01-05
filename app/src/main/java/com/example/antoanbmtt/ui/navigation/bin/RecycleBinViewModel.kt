package com.example.antoanbmtt.ui.navigation.bin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antoanbmtt.api.ApiResult
import com.example.antoanbmtt.api.resource.ResourceContent
import com.example.antoanbmtt.repository.resource.ResourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecycleBinViewModel @Inject constructor(
    private val resourceRepository: ResourceRepository
) : ViewModel(){
    private val _recycleBinUiState = MutableLiveData(RecycleBinUiState())
    val recycleBinUiState : LiveData<RecycleBinUiState> get() = _recycleBinUiState
    init{
        getResources()
    }
    private val _resourceContent = MutableLiveData(ResourceContent())
    val resourceContent : LiveData<ResourceContent> get() = _resourceContent
    fun getResources(){
        viewModelScope.launch {
            _recycleBinUiState.value = _recycleBinUiState.value?.copy(isLoading = true)
            when(val result = resourceRepository.getTempDeleteResources()){
                is ApiResult.Success -> _recycleBinUiState.value = _recycleBinUiState.value?.copy(resources = result.data, isLoading = false)
                is ApiResult.Failure -> _recycleBinUiState.value = _recycleBinUiState.value?.copy(errorMessage = result.message, isLoading = false)
            }
        }
    }
    fun getResourceContent(uri : String){
        _recycleBinUiState.value = _recycleBinUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.getResourceContent(uri)){
                is ApiResult.Success -> {
                    _resourceContent.value = result.data
                    _recycleBinUiState.value = _recycleBinUiState.value?.copy(isLoading = false)
                }
                is ApiResult.Failure -> _recycleBinUiState.value = _recycleBinUiState.value?.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
    fun messageShown(){
        _recycleBinUiState.value = _recycleBinUiState.value?.copy(errorMessage = null)
    }
    fun updateTempDelete(id : Long,isTempDelete : Boolean){
        val fields = "{\"is_temp_delete\":\"${isTempDelete}\"}";
        _recycleBinUiState.value = _recycleBinUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.restoreResourceFromTempDelete(fields,id)){
                is ApiResult.Success -> {
                    _recycleBinUiState.value = _recycleBinUiState.value?.copy(isLoading = false, resources = result.data)
                    _recycleBinUiState.value = _recycleBinUiState.value?.copy(isLoading = false)
                }
                is ApiResult.Failure -> _recycleBinUiState.value = _recycleBinUiState.value?.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
    fun resetContent(){
        _resourceContent.value = ResourceContent()
    }
}