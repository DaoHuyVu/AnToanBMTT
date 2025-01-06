package com.example.antoanbmtt.ui.navigation.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antoanbmtt.api.account.AccountService
import com.example.antoanbmtt.api.user.UserService
import com.example.antoanbmtt.repository.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val dataStore: UserDataStore,
    private val userService: UserService,
    private val accountService: AccountService
) : ViewModel(){
    private val _email = MutableLiveData(dataStore.getEmail()!!)
    val email : LiveData<String> get() = _email
    private val _userName = MutableLiveData(dataStore.getUserName()!!)
    val userName : LiveData<String> get() = _userName
    private val _password = MutableLiveData<String?>(null)
    val password : LiveData<String?> get() = _password
    private val _isLoading = MutableLiveData(false)
    val isLoading : LiveData<Boolean> get() = _isLoading
    private val _message = MutableLiveData<String?>(null)
    val message : LiveData<String?> get() = _message

    fun changePassword(old : String, new : String){
        _isLoading.value = true
        if(old == new){
            _message.value = "New password must be different to the old one"
        }
        else{
            viewModelScope.launch {
                val result = accountService.updatePassword(old,new,_email.value!!)
                if(result.isSuccessful){
                    _message.value = "Change password successfully"
                }
                else{
                    _message.value = "Change password failed"
                }
            }
        }
        _isLoading.value = false
    }
    fun changeEmail(old : String, new : String){
        _isLoading.value = true
        if(old == new){
            _message.value = "New email must be different to the old one"
        }
        else if(old !== _email.value){
            _message.value = "Old email does not match"
        }
        else{
            viewModelScope.launch {
                val result = accountService.updateEmail(new,old)
                if(result.isSuccessful){
                    _message.value = "Change email successfully"
                }
                else{
                    _message.value = "Change email failed"
                }
            }
        }
        _isLoading.value = false
    }
    fun changeUserName(new : String){
        _isLoading.value = true
        viewModelScope.launch {
                val result = userService.updateUserName(new,_email.value!!)
                if(result.isSuccessful){
                    _message.value = "Change user name successfully"
                }
                else{
                    _message.value = "Change user name failed"
                }
            }
        _isLoading.value = false
    }
    fun messageShown(){
        _message.value = null
    }
}