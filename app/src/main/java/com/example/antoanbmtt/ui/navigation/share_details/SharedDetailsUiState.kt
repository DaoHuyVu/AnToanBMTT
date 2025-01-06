package com.example.antoanbmtt.ui.navigation.share_details

import com.example.antoanbmtt.repository.resource.ResourceDetails

data class SharedDetailsUiState(
    val resource : ResourceDetails? = null,
    val isLoading : Boolean = false,
    val errorMessage : String? = null,
    val password : String? = null
)
