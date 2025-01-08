package com.example.antoanbmtt.ui.navigation.link

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antoanbmtt.api.ApiResult
import com.example.antoanbmtt.repository.resource.ResourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LinkViewModel @Inject constructor(
    private val resourceRepository: ResourceRepository
) : ViewModel() {
    private val _linkUiState = MutableLiveData(LinkUiState())
    val linkUiState: LiveData<LinkUiState> get() = _linkUiState
    private val _inputUri = MutableLiveData<String?>(null)
    val inputUri: LiveData<String?> get() = _inputUri
    private val _password = MutableLiveData<String?>(null)
    val password: LiveData<String?> get() = _password
    fun getSharedItem() {
        _linkUiState.value = _linkUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = resourceRepository.getSharedResource("${_inputUri.value!!}#${_password.value ?: ""}")) {
                is ApiResult.Success -> {
                    _linkUiState.value =
                        _linkUiState.value?.copy(isLoading = false, resource = result.data)
                }

                is ApiResult.Failure -> {
                    _linkUiState.value =
                        _linkUiState.value?.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }

    fun uriChange(u: String) {
        _inputUri.value = u
    }
    fun passwordChange(p : String){
        _password.value = p
    }
    fun messageShown() {
        _linkUiState.value = _linkUiState.value?.copy(errorMessage = null)
    }
    fun getSharedContent() {
        _linkUiState.value = _linkUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = resourceRepository.getSharedResourceContent("${_inputUri.value!!}#${_password.value ?:""}")) {
                is ApiResult.Success -> {
                    _linkUiState.value =
                        _linkUiState.value?.copy(isLoading = false, resourceContent = result.data)
                }
                is ApiResult.Failure -> {
                    _linkUiState.value =
                        _linkUiState.value?.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }
    fun downloadContent(){
        _linkUiState.value = _linkUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = resourceRepository.getSharedResourceContent("${_inputUri.value!!}#${_password.value ?: ""}")) {
                is ApiResult.Success -> {
                    _linkUiState.value =
                        _linkUiState.value?.copy(isLoading = false, downloadContent = result.data)
                }

                is ApiResult.Failure -> {
                    _linkUiState.value =
                        _linkUiState.value?.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }
    fun resetContent(){
        _linkUiState.value = _linkUiState.value?.copy(resourceContent = null)
    }
    fun resetInfo(){
        _linkUiState.value = _linkUiState.value?.copy(resource = null)
    }
    fun resetDownloadContent(){
        _linkUiState.value = _linkUiState.value?.copy(downloadContent = null)
    }
}