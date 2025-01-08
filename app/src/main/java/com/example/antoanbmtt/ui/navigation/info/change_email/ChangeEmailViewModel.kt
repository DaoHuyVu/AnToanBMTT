package com.example.antoanbmtt.ui.navigation.info.change_email

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antoanbmtt.api.account.AccountService
import com.example.antoanbmtt.repository.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
    private val accountService: AccountService
) : ViewModel(){
    private val _email = MutableLiveData(userDataStore.getEmail()!!)
    val email : LiveData<String> get() = _email
    private val _isSuccessful = MutableLiveData(false)
    val isSuccessful : LiveData<Boolean> get() = _isSuccessful
    private val _message = MutableLiveData<String?>(null)
    val message : LiveData<String?> get() = _message
    fun changeEmail(new : String){
        val old = userDataStore.getEmail()!!
        if(old == new){
            _message.value = "New email must be different to the old one"
        }
        else{
            viewModelScope.launch {
                val result = accountService.updateEmail(new,old)
                if(result.isSuccessful){
                    userDataStore.updateEmail(new)
                    _message.value = "Change email successfully"
                    _isSuccessful.value = true
                }
                else{
                    _message.value = "Change email failed"
                }
            }
        }
    }

    fun messageShown(){
        _message.value = null
    }
}