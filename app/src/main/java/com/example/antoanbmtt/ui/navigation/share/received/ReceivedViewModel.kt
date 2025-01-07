package com.example.antoanbmtt.ui.navigation.share.received
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antoanbmtt.api.ApiResult
import com.example.antoanbmtt.api.resource.DownloadResourceContent
import com.example.antoanbmtt.api.resource.ResourceContent
import com.example.antoanbmtt.repository.resource.ResourceRepository
import com.example.antoanbmtt.ui.navigation.cloud.CloudStorageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReceivedViewModel @Inject constructor(
    private val resourceRepository: ResourceRepository
) : ViewModel() {
    private val _receivedUiState = MutableLiveData(ReceivedUiState())
    val receivedUiState : LiveData<ReceivedUiState> get() = _receivedUiState
    init{
        getResources()
    }
    private val _resourceContent = MutableLiveData(ResourceContent())
    val resourceContent : LiveData<ResourceContent> get() = _resourceContent
    private val _downloadContent = MutableLiveData(DownloadResourceContent())
    val downloadContent : LiveData<DownloadResourceContent> get() = _downloadContent
    fun getResources(){
        _receivedUiState.value = _receivedUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.getReceivedResources()){
                is ApiResult.Success -> {
                    _receivedUiState.value = _receivedUiState.value?.copy(isLoading = false, receivedResources = result.data)
                }
                is ApiResult.Failure -> _receivedUiState.value = _receivedUiState.value?.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
    fun messageShown(){
        _receivedUiState.value = _receivedUiState.value?.copy(errorMessage = null)
    }
    fun getResourceContent(uri : String){
        _receivedUiState.value = _receivedUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.getResourceContent(uri)){
                is ApiResult.Success -> {
                    _resourceContent.value = result.data
                    _receivedUiState.value = _receivedUiState.value?.copy(isLoading = false)
                }
                is ApiResult.Failure -> _receivedUiState.value = _receivedUiState.value?.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
    fun downloadContent(uri : String,fileName : String){
        _receivedUiState.value = _receivedUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.downloadContent(uri,fileName)){
                is ApiResult.Success -> {
                    _downloadContent.value = result.data
                    _receivedUiState.value = _receivedUiState.value?.copy(isLoading = false)
                }
                is ApiResult.Failure -> _receivedUiState.value = _receivedUiState.value?.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
    fun resetContent(){
        _resourceContent.value = ResourceContent()
    }
    fun resetDownloadContent(){
        _downloadContent.value = DownloadResourceContent()
    }
}