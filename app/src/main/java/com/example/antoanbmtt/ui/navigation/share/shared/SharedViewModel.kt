package com.example.antoanbmtt.ui.navigation.share.shared
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
class SharedViewModel @Inject constructor(
    private val resourceRepository: ResourceRepository
) : ViewModel() {
    private val _sharedUiState = MutableLiveData(SharedUiState())
    val sharedUiState : LiveData<SharedUiState> get() = _sharedUiState
    init{
        getResources()
    }
    private val _resourceContent = MutableLiveData(ResourceContent())
    val resourceContent : LiveData<ResourceContent> get() = _resourceContent
    private val _downloadContent = MutableLiveData(DownloadResourceContent())
    val downloadContent : LiveData<DownloadResourceContent> get() = _downloadContent
    fun getResources(){
        _sharedUiState.value = _sharedUiState.value?.copy(resources = resourceRepository.getSharedResources())
    }
    fun messageShown(){
        _sharedUiState.value = _sharedUiState.value?.copy(errorMessage = null)
    }
    fun getResourceContent(uri : String){
        _sharedUiState.value = _sharedUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.getResourceContent(uri)){
                is ApiResult.Success -> {
                    _resourceContent.value = result.data
                    _sharedUiState.value = _sharedUiState.value?.copy(isLoading = false)
                }
                is ApiResult.Failure -> _sharedUiState.value = _sharedUiState.value?.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
    fun downloadContent(uri : String,fileName : String){
        _sharedUiState.value = _sharedUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.downloadContent(uri,fileName)){
                is ApiResult.Success -> {
                    _downloadContent.value = result.data
                    _sharedUiState.value = _sharedUiState.value?.copy(isLoading = false)
                }
                is ApiResult.Failure -> _sharedUiState.value = _sharedUiState.value?.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
    fun updateFavourite(id : Long,isFavourite : Boolean){
        val fields = "{\"is_favourite\":$isFavourite}"
        _sharedUiState.value = _sharedUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.updateFavourite(fields,id)){
                is ApiResult.Success -> {
                    _sharedUiState.value = _sharedUiState.value?.copy(isLoading = false, resources = result.data)
                    _sharedUiState.value = _sharedUiState.value?.copy(isLoading = false)
                }
                is ApiResult.Failure -> _sharedUiState.value = _sharedUiState.value?.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
    fun updateTempDelete(id : Long,isTempDelete : Boolean){
        val fields = "{\"is_temp_delete\":\"${isTempDelete}\"}";
        _sharedUiState.value = _sharedUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.tempDeleteResource(fields,id)){
                is ApiResult.Success -> {
                    _sharedUiState.value = _sharedUiState.value?.copy(isLoading = false, resources = result.data)
                    _sharedUiState.value = _sharedUiState.value?.copy(isLoading = false)
                }
                is ApiResult.Failure -> _sharedUiState.value = _sharedUiState.value?.copy(isLoading = false, errorMessage = result.message)
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