package com.example.antoanbmtt.ui.navigation.share.received

import com.example.antoanbmtt.api.resource.SharedResource

data class ReceivedUiState(
    val receivedResources: List<SharedResource> = emptyList(),
    val isLoading : Boolean = false,
    val errorMessage : String? = null
)