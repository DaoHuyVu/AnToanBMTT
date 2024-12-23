package com.example.antoanbmtt.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharePreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
    fun saveUserInfo(email : String,userName : String,token : String){
        sharePreferences.edit().apply{
            putString("userName",userName)
            putString("email",email)
            putString("token",token)
            apply()
        }
    }
    fun getUserName() = sharePreferences.getString("userName",null)
    fun getEmail() = sharePreferences.getString("email",null)
    fun getToken() = sharePreferences.getString("token",null)

    fun deleteUserInfo(){
        sharePreferences.edit().apply{
            clear()
            apply()
        }
    }
}