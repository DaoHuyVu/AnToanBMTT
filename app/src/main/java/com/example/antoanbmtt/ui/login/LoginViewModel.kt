package com.example.antoanbmtt.ui.login
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antoanbmtt.api.login.AuthService
import com.example.antoanbmtt.api.login.LoginRequest
import com.example.antoanbmtt.repository.UserDataStore
import com.example.antoanbmtt.security.KeyPairUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService : AuthService,
    private val dataStore : UserDataStore,
    private val keyPairUtil: KeyPairUtil
) : ViewModel(){
    private val _loginState = MutableLiveData(LoginUiState())
    val loginState : LiveData<LoginUiState> get() = _loginState
    init{
        if(dataStore.getEmailBiometrics() != null){
            _loginState.value = _loginState.value?.copy(isBiometricsAuthenticated = true, usingBiometricsAuth = true, userName = dataStore.getEmailBiometrics())
        }
    }
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
                val response = authService.login(LoginRequest(_loginState.value!!.userName!!.trim(),_loginState.value!!.password!!.trim()))
                if(response.isSuccessful){
                    val successResponse = response.body()
                    dataStore.saveUserInfo(successResponse!!.userInfo.email,successResponse.userInfo.userName,successResponse.accessToken)
                    exchangeKey()
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
    fun loginBiometrics(){
        _loginState.value = _loginState.value?.copy(passwordFieldEmpty = false, emailFieldEmpty = false, isLoading = true)
        viewModelScope.launch {
            val response = authService.loginBiometrics(dataStore.getEmailBiometrics()!!,dataStore.getUserNameBiometrics()!!)
            if(response.isSuccessful){
                val successResponse = response.body()
                dataStore.saveUserInfo(successResponse!!.userInfo.email,successResponse.userInfo.userName,successResponse.accessToken)
                exchangeKey()
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
    private fun exchangeKey(){
        viewModelScope.launch {
            val response = authService.exchangePublicKey(keyPairUtil.publicKey.toString(),_loginState.value!!.userName!!.trim())
            if(response.isSuccessful){
                dataStore.setPublicKey(response.body()!!)
                _loginState.value = _loginState.value?.copy(isLoading = false, isLoginSuccessfully = true)
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