package com.example.antoanbmtt.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antoanbmtt.api.login.AccountService
import com.example.antoanbmtt.api.login.SignUpRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel(){
    private val _signUpUiState = MutableLiveData(SignUpUiState())
    val signUpUiState : LiveData<SignUpUiState> get() = _signUpUiState
    fun signUp(){
        if(_signUpUiState.value?.userName.isNullOrEmpty() || _signUpUiState.value?.password.isNullOrEmpty() || _signUpUiState.value?.confirmPassword.isNullOrEmpty() || _signUpUiState.value?.email.isNullOrEmpty()){
            var userNameEmpty = false
            var passwordEmpty = false
            var emailEmpty = false
            var confirmPassword = false
            if(_signUpUiState.value?.userName.isNullOrEmpty()){
                userNameEmpty = true
            }
            if(_signUpUiState.value?.password.isNullOrEmpty()){
                passwordEmpty = true
            }
            if(_signUpUiState.value?.email.isNullOrEmpty()){
                emailEmpty = true
            }
            if(_signUpUiState.value?.confirmPassword.isNullOrEmpty()){
                confirmPassword = true
            }
            _signUpUiState.value = _signUpUiState.value?.copy(
                userNameFieldEmpty = userNameEmpty,
                passwordFieldEmpty = passwordEmpty,
                emailFieldEmpty = emailEmpty,
                confirmPasswordFieldEmpty = confirmPassword
            )
        }
        else{
            _signUpUiState.value = _signUpUiState.value?.copy(emailFieldEmpty = false, passwordFieldEmpty = false, confirmPasswordFieldEmpty = false, userNameFieldEmpty = false)
            if(_signUpUiState.value?.confirmPassword?.equals(_signUpUiState.value?.password) == false){
                _signUpUiState.value = _signUpUiState.value?.copy(confirmPasswordNotMatch = true)
            }else{
                _signUpUiState.value = _signUpUiState.value?.copy(isLoading = true, confirmPasswordNotMatch = true)
                viewModelScope.launch {
                    val response = accountService.signUp(SignUpRequest(_signUpUiState.value!!.userName!!.trim(),_signUpUiState.value!!.email!!.trim(),_signUpUiState.value!!.password!!.trim()))
                    if(response.isSuccessful){
                        _signUpUiState.value = _signUpUiState.value?.copy(isLoading = false, isSignUpSuccessfully = true)
                    }
                    else{
                        if(response.code() == 409){
                            _signUpUiState.value = _signUpUiState.value?.copy(isLoading = false, message = "This email has already been used")
                        }
                        else
                            _signUpUiState.value = _signUpUiState.value?.copy(isLoading = false, message = "Host error")
                    }
                }
            }
        }
    }
    fun userNameChange(userName : String){
        _signUpUiState.value = _signUpUiState.value?.copy(userName = userName)
    }
    fun passwordChange(p : String){
        _signUpUiState.value = _signUpUiState.value?.copy(password = p)
    }
    fun emailChange(e : String){
        _signUpUiState.value = _signUpUiState.value?.copy(email = e)
    }
    fun confirmPasswordChange(cf : String){
        _signUpUiState.value = _signUpUiState.value?.copy(confirmPassword = cf)
    }
    fun messageShown(){
        _signUpUiState.value = _signUpUiState.value?.copy(message = null)
    }
}