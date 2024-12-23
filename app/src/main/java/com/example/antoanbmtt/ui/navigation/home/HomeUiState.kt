package com.example.antoanbmtt.ui.navigation.home

import com.example.antoanbmtt.repository.Resource

data class HomeUiState(
    val resources : List<Resource> = emptyList(),
    val isLoading : Boolean = false,
    val errorMessage : String? = null
)