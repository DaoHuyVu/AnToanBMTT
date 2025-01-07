package com.example.antoanbmtt.repository

import android.content.Context
import android.content.SharedPreferences
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
    fun setFingerprintEnable(isEnable : Boolean){
        sharePreferences.edit().apply{
            putBoolean("isFingerprintEnable",isEnable)
            apply()
        }
    }
    fun fingerprintEnable() = sharePreferences.getBoolean("isFingerprintEnable",false)
    fun setPinEnable(isEnable: Boolean){
        sharePreferences.edit().apply{
            putBoolean("isPinEnable",isEnable)
            apply()
        }
    }
    fun pinEnable() = sharePreferences.getBoolean("isPinEnable",false)
    fun setEmail(email : String) = sharePreferences.edit().apply{
        putString("email",email)
        apply()
    }
    fun setUserName(userName : String) = sharePreferences.edit().apply{
        putString("userName",userName)
        apply()
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