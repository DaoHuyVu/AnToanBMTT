package com.example.antoanbmtt.ui.navigation.details

import com.example.antoanbmtt.repository.resource.ResourceDetails

data class ResourceDetailsUiState(
    val resource : ResourceDetails? = null,
    val isLoading : Boolean = false,
    val errorMessage : String? = null
)
