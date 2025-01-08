package com.example.antoanbmtt.ui.navigation.info

import android.util.Log
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

}