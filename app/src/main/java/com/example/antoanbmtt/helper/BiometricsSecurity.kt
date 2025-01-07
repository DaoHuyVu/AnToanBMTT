package com.example.antoanbmtt.helper

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.antoanbmtt.repository.UserDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BiometricsSecurity @Inject constructor(
    private val userDataStore: UserDataStore
){
    fun setUpBiometricsAuthentication(
        fragment : Fragment,
        onSucceed : () -> Unit,
    ){
        val executor = ContextCompat.getMainExecutor(fragment.requireActivity())
        val biometricPrompt = BiometricPrompt(fragment, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                userDataStore.activateFingerprint()
                onSucceed.invoke()
            }
        })
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Setup Biometric")
            .setSubtitle("Setting up authentication using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()
        biometricPrompt.authenticate(promptInfo)
    }
    fun authenticate(fragment: Fragment,onSucceed: () -> Unit){
        val executor = ContextCompat.getMainExecutor(fragment.requireActivity())
        val biometricPrompt = BiometricPrompt(fragment, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSucceed.invoke()
            }
        })
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authentication required")
            .setSubtitle("Verify identity")
            .setDescription("Scan fingerprint to continue")
            .setNegativeButtonText("Cancel")
            .build()
        biometricPrompt.authenticate(promptInfo)
    }
    fun fingerprintAvailable(context : Context) : Boolean{
        val biometricsManager = BiometricManager.from(context)
        return when (biometricsManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Fingerprint authentication is available and the user has enrolled fingerprints
                true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                // Device doesn't have a fingerprint sensor
                false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                // Fingerprint hardware is unavailable (e.g., temporarily disabled)
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // The user hasn't enrolled any fingerprints
                false
            }
            else -> {
                // Other errors, handle appropriately
                false
            }
        }
    }
}