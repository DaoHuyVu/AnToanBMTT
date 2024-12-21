package com.example.antoanbmtt.api

import okhttp3.ResponseBody

sealed class ApiResponse<out E>{
    data class Success<T>(val data : T) : ApiResponse<T>()
    data class Failure(val message : String) : ApiResponse<Nothing>()
}
