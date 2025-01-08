package com.example.antoanbmtt.ui.navigation.info.change_userinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antoanbmtt.api.user.UserService
import com.example.antoanbmtt.repository.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeUserInfoViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
    private val userService: UserService
) : ViewModel(){
    private val _message = MutableLiveData<String?>(null)
    val message : LiveData<String?> get() = _message
    private val _isSuccessful = MutableLiveData(false)
    val isSuccessful : LiveData<Boolean> get() = _isSuccessful
    fun changeUserName(new : String){
        viewModelScope.launch {
            val result = userService.updateUserName(new,userDataStore.getEmail()!!)
            if(result.isSuccessful){
                userDataStore.updateUserName(new)
                _message.value = "Change user name successfully"
                _isSuccessful.value = true
            }
            else{
                _message.value = "Change user name failed"
            }
        }
    }
    fun messageShown(){
        _message.value = null
    }
}