package com.example.antoanbmtt.ui.navigation.share_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.antoanbmtt.api.ApiResult
import com.example.antoanbmtt.repository.resource.ResourceRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class ShareDetailsViewModel @AssistedInject constructor(
    @Assisted val resourceId : Long,
    private val resourceRepository: ResourceRepository
) : ViewModel(){
    private val _sharedDetailsUiState = MutableLiveData(SharedDetailsUiState())
    val sharedDetailsUiState : LiveData<SharedDetailsUiState> get() = _sharedDetailsUiState
    init{
        getSharedItem()
    }
    private fun getSharedItem(){
        _sharedDetailsUiState.value = _sharedDetailsUiState.value?.copy(isLoading = true)
        when(val result = resourceRepository.getResourceItem(resourceId)){
            is ApiResult.Success -> {
                _sharedDetailsUiState.value = _sharedDetailsUiState.value?.copy(isLoading = false, resource = result.data)
            }
            is ApiResult.Failure -> {
                _sharedDetailsUiState.value = _sharedDetailsUiState.value?.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
    fun passwordChange(p : String){
        _sharedDetailsUiState.value = _sharedDetailsUiState.value?.copy(password = p)
    }
    fun messageShown(){
        _sharedDetailsUiState.value = _sharedDetailsUiState.value?.copy(errorMessage = null)
    }
    fun updatePassword(){
        _sharedDetailsUiState.value = _sharedDetailsUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.updateSharedPassword(sharedDetailsUiState.value?.resource!!.id,sharedDetailsUiState.value?.password!!)){
                is ApiResult.Success -> {
                    _sharedDetailsUiState.value = _sharedDetailsUiState.value?.copy(isLoading = false, resource = result.data, password = null)
                }
                is ApiResult.Failure -> {
                    _sharedDetailsUiState.value = _sharedDetailsUiState.value?.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }
    fun deleteLink(){

    }
    @AssistedFactory
    interface SharedDetailsViewModelFactory{
        fun create(resourceId : Long) : ShareDetailsViewModel
    }
    companion object {
        fun provideItemDetailFactory(
            factory: SharedDetailsViewModelFactory,
            itemId: Long
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("Unchecked_cast")
                return factory.create(itemId) as T
            }
        }
    }
}