package com.example.antoanbmtt.ui.navigation.bin

import com.example.antoanbmtt.repository.resource.Resource

data class RecycleBinUiState(
    val resources : List<Resource> = emptyList(),
    val isLoading : Boolean = false,
    val errorMessage : String? = null
)
