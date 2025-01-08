package com.example.antoanbmtt.ui.navigation.info.change_password

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antoanbmtt.api.account.AccountService
import com.example.antoanbmtt.repository.UserDataStore
import com.example.antoanbmtt.security.KeyPairUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val accountService: AccountService,
    private val dataStore: UserDataStore,
    private val keyPairUtil: KeyPairUtil
) : ViewModel(){
    private val _message = MutableLiveData<String?>(null)
    val message : LiveData<String?> get() = _message
    private val _isSuccessful = MutableLiveData(false)
    val isSuccessful : LiveData<Boolean> get() = _isSuccessful
    fun changePassword(old : String, new : String){
        if(old == new){
            _message.value = "New password must be different to the old one"
        }
        else{
            viewModelScope.launch {
                val oldEnc = keyPairUtil.encWithPublicKey(old)
                val newEnc = keyPairUtil.encWithPublicKey(new)
                val emailEnc = keyPairUtil.encWithPublicKey(dataStore.getEmail()!!)
                val result = accountService.updatePassword(oldEnc,newEnc,emailEnc)
                Log.d("CHANGE_PASSWORD","$oldEnc $newEnc $emailEnc")
                if(result.isSuccessful){
                    _message.value = "Change password successfully"
                    _isSuccessful.value = true
                }
                else{
                    _message.value = "Change password failed"
                }
            }
        }
    }
    fun messageShown(){
        _message.value = null
    }
}