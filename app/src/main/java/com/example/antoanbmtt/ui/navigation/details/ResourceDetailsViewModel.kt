package com.example.antoanbmtt.ui.navigation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.antoanbmtt.api.ApiResult
import com.example.antoanbmtt.repository.resource.ResourceRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class ResourceDetailsViewModel @AssistedInject constructor(
    @Assisted val resourceId : Long,
    private val resourceRepository: ResourceRepository
) : ViewModel(){
    private val _resourceDetailsUiState = MutableLiveData(ResourceDetailsUiState())
    val resourceDetailsUiState : LiveData<ResourceDetailsUiState> get() = _resourceDetailsUiState
    init{
        getResourceItem()
    }
    private fun getResourceItem(){
        _resourceDetailsUiState.value = _resourceDetailsUiState.value?.copy(isLoading = true)
        when(val result = resourceRepository.getResourceItem(resourceId)){
            is ApiResult.Success -> {
                _resourceDetailsUiState.value = _resourceDetailsUiState.value?.copy(isLoading = false, resource = result.data)
            }
            is ApiResult.Failure -> {
                _resourceDetailsUiState.value = _resourceDetailsUiState.value?.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
    fun messageShown(){
        _resourceDetailsUiState.value = _resourceDetailsUiState.value?.copy(errorMessage = null)
    }
    @AssistedFactory
    interface ResourceDetailsViewModelFactory{
        fun create(resourceId : Long) : ResourceDetailsViewModel
    }
    companion object {
        fun provideItemDetailFactory(
            factory: ResourceDetailsViewModelFactory,
            itemId: Long
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("Unchecked_cast")
                return factory.create(itemId) as T
            }
        }
    }
}

