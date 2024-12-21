package com.example.antoanbmtt.ui.login
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antoanbmtt.api.ApiResponse
import com.example.antoanbmtt.api.login.AccountService
import com.example.antoanbmtt.api.login.LoginRequest
import com.example.antoanbmtt.api.login.LoginResponse
import com.example.antoanbmtt.repository.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService : AccountService,
    private val dataStore : UserDataStore
) : ViewModel(){
    private val _loginState = MutableLiveData(LoginUiState())
    val loginState : LiveData<LoginUiState> get() = _loginState
    fun login(){
        if(_loginState.value?.userName.isNullOrEmpty() || _loginState.value?.password.isNullOrEmpty()){
            if(_loginState.value?.userName.isNullOrEmpty() && _loginState.value?.password.isNullOrEmpty()){
                _loginState.value = _loginState.value?.copy(emailFieldEmpty = true, passwordFieldEmpty = true)
            }
            else{
                if(_loginState.value?.userName.isNullOrEmpty()){
                    _loginState.value = _loginState.value?.copy(emailFieldEmpty = true)
                    _loginState.value = _loginState.value?.copy(passwordFieldEmpty = false)
                }
                else
                    _loginState.value = _loginState.value?.copy(passwordFieldEmpty = true)
                _loginState.value = _loginState.value?.copy(emailFieldEmpty = false)

            }
        }
        else{
            _loginState.value = _loginState.value?.copy(isLoading = true, emailFieldEmpty = false, passwordFieldEmpty = false)
            viewModelScope.launch {
                val response = accountService.login(LoginRequest(_loginState.value!!.userName!!.trim(),_loginState.value!!.password!!.trim()))
                if(response.isSuccessful){
                    val successResponse = response.body()
                    dataStore.addEmail(successResponse!!.email)
                    dataStore.addUserName(successResponse.userName)
                    dataStore.addAccessToken(successResponse.accessToken)
                    _loginState.value = _loginState.value?.copy(isLoading = false, isLoginSuccessfully = true)
                }
                else{
                    if(response.code() == 443){
                        _loginState.value = _loginState.value?.copy(isLoading = false, message = "Username or password incorrect")
                    }
                    else{
                        _loginState.value = _loginState.value?.copy(isLoading = false, message = "Host error")
                    }
                }
            }
        }
    }
    fun userNameChange(userName : String){
        _loginState.value = _loginState.value?.copy(userName = userName)
    }
    fun passwordChange(p : String){
        _loginState.value = _loginState.value?.copy(password = p)
    }
    fun messageShown(){
        _loginState.value = _loginState.value?.copy(message = null)
    }
}