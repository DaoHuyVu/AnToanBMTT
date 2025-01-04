package com.example.antoanbmtt.ui.navigation.cloud
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antoanbmtt.api.ApiResult
import com.example.antoanbmtt.api.resource.DownloadResourceContent
import com.example.antoanbmtt.api.resource.ResourceContent
import com.example.antoanbmtt.repository.resource.ResourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
class CloudStorageViewModel @Inject constructor(
    private val resourceRepository: ResourceRepository
) : ViewModel() {
    private val _cloudStorageUiState = MutableLiveData(CloudStorageUiState())
    val cloudStorageUiState : LiveData<CloudStorageUiState> get() = _cloudStorageUiState
    init{
        getResources()
    }
    private val _resourceContent = MutableLiveData(ResourceContent())
    val resourceContent : LiveData<ResourceContent> get() = _resourceContent
    private val _downloadContent = MutableLiveData(DownloadResourceContent())
    val downloadContent : LiveData<DownloadResourceContent> get() = _downloadContent
    fun pushResource(file : MultipartBody.Part){
        _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.postResource(file)){
                is ApiResult.Success -> _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(resources = result.data, isLoading = false)
                is ApiResult.Failure -> _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(errorMessage = result.message, isLoading = false)
            }
        }
    }
    fun getResources(){
        viewModelScope.launch {
            _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = true)
            when(val result = resourceRepository.getResources()){
                is ApiResult.Success -> _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(resources = result.data, isLoading = false)
                is ApiResult.Failure -> _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(errorMessage = result.message, isLoading = false)
            }
        }
    }
    fun messageShown(){
        _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(errorMessage = null)
    }
    fun getResourceContent(uri : String){
        _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.getResourceContent(uri)){
                is ApiResult.Success -> {
                    _resourceContent.value = result.data
                    _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = false)
                }
                is ApiResult.Failure -> _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
    fun downloadContent(uri : String,fileName : String){
        _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.downloadContent(uri,fileName)){
                is ApiResult.Success -> {
                    _downloadContent.value = result.data
                    _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = false)
                }
                is ApiResult.Failure -> _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
    fun updateFavourite(id : Long,isFavourite : Boolean){
        val fields = "{\"is_favourite\":$isFavourite}"
        _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.tempDeleteResource(fields,id)){
                is ApiResult.Success -> {
                    _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = false, resources = result.data)
                    _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = false)
                }
                is ApiResult.Failure -> _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }
    fun updateTempDelete(id : Long,isTempDelete : Boolean){
        val fields = "{\"is_temp_delete\":\"${isTempDelete}\"}";
        _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = resourceRepository.tempDeleteResource(fields,id)){
                is ApiResult.Success -> {
                    _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = false, resources = result.data)
                    _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = false)
                }
                is ApiResult.Failure -> _cloudStorageUiState.value = _cloudStorageUiState.value?.copy(isLoading = false, errorMessage = result.message)
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