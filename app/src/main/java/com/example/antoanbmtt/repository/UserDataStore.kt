package com.example.antoanbmtt.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataStore @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "userData")
    private val accessToken = stringPreferencesKey("accessToken")
    private val userName = stringPreferencesKey("userName")
    private val email = stringPreferencesKey("email")
    suspend fun addAccessToken(token : String){
        context.dataStore.edit { preferences ->
            preferences[accessToken] = token
        }
    }
    suspend fun addUserName(name : String){
        context.dataStore.edit { preferences ->
            preferences[userName] = name
        }
    }
    suspend fun addEmail(mail : String){
        context.dataStore.edit { preferences ->
            preferences[userName] = mail
        }
    }
    val tokenFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            // On the first run of the app, we will use LinearLayoutManager by default
            preferences[accessToken] ?: ""
        }
    val userNameFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            // On the first run of the app, we will use LinearLayoutManager by default
            preferences[userName] ?: ""
        }
    val emailFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            // On the first run of the app, we will use LinearLayoutManager by default
            preferences[email] ?: ""
        }
}