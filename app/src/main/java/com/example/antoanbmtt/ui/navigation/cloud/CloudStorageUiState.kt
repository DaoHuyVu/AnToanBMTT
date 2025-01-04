package com.example.antoanbmtt.ui.navigation.cloud

import com.example.antoanbmtt.repository.resource.Resource

data class CloudStorageUiState(
    val resources : List<Resource> = emptyList(),
    val isLoading : Boolean = false,
    val errorMessage : String? = null
)