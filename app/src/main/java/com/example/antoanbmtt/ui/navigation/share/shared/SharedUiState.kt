package com.example.antoanbmtt.ui.navigation.share.shared

import com.example.antoanbmtt.repository.resource.Resource

data class SharedUiState(
    val resources : List<Resource> = emptyList(),
    val isLoading : Boolean = false,
    val errorMessage : String? = null
)
