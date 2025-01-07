package com.example.antoanbmtt.ui.login

data class LoginUiState(
    val userName : String? = null,
    val password : String? = null,
    val isLoading : Boolean = false,
    val emailFieldEmpty : Boolean = false,
    val passwordFieldEmpty : Boolean = false,
    val isLoginSuccessfully : Boolean = false,
    val message : String? = null,
    val isBiometricsAuthenticated : Boolean = false,
    val usingBiometricsAuth : Boolean = false
)