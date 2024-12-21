package com.example.antoanbmtt.ui.signup

data class SignUpUiState(
    val userName : String? = null,
    val email : String? = null,
    val password : String? = null,
    val confirmPassword : String? = null,
    val isLoading : Boolean = false,
    val emailFieldEmpty : Boolean = false,
    val passwordFieldEmpty : Boolean = false,
    val userNameFieldEmpty: Boolean = false,
    val confirmPasswordNotMatch : Boolean = false,
    val confirmPasswordFieldEmpty: Boolean = false,
    val isSignUpSuccessfully : Boolean = false,
    val message : String? = null
)
