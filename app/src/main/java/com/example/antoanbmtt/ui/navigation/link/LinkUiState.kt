package com.example.antoanbmtt.ui.navigation.link

import com.example.antoanbmtt.api.resource.ResourceContent
import com.example.antoanbmtt.api.resource.SharedResource
import com.example.antoanbmtt.repository.resource.ResourceDetails
import okhttp3.ResponseBody

data class LinkUiState(
    val resource : SharedResource? = null,
    val isLoading : Boolean = false,
    val errorMessage : String? = null,
    val resourceContent: ResponseBody? = null,
    val downloadContent : ResponseBody? = null,
)
