package com.example.antoanbmtt.api

sealed class ApiResult<out E>{
    data class Success<T>(val data : T) : ApiResult<T>()
    data class Failure(val message : String) : ApiResult<Nothing>()
}
