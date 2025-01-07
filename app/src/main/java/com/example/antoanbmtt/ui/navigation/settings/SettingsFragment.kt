package com.example.antoanbmtt.ui.navigation.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.antoanbmtt.R
import com.example.antoanbmtt.helper.BiometricsSecurity
import com.example.antoanbmtt.helper.showToast
import com.example.antoanbmtt.repository.UserDataStore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {
    private var fingerPrintPreference : SwitchPreferenceCompat? = null
    private var pinPreference : SwitchPreferenceCompat? = null
    private var changePinPreference : Preference? = null
    @Inject lateinit var biometricsSecurity : BiometricsSecurity
    @Inject lateinit var userDataStore: UserDataStore
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences,rootKey)
        val fingerprintAvailable = biometricsSecurity.fingerprintAvailable(requireContext())
        fingerPrintPreference = findPreference("biometric")
        pinPreference = findPreference("pin")
        changePinPreference = findPreference("changePin")

        pinPreference?.isChecked = userDataStore.pinEnable()
        fingerPrintPreference?.isVisible = pinPreference?.isChecked == true
        changePinPreference?.isVisible = pinPreference?.isChecked == true

        if(fingerprintAvailable){
            fingerPrintPreference?.setOnPreferenceChangeListener{ _, newValue ->
                val value = newValue as Boolean
                if(value){
                    biometricsSecurity.setUpBiometricsAuthentication(this
                    ) {
                        userDataStore.setFingerprintEnable(true)
                        fingerPrintPreference?.isChecked = true
                        showToast("Your fingerprint has been authenticated")
                    }
                }
                else{
                    userDataStore.setFingerprintEnable(false)
                }
                false
            }
        }
        else{
            fingerPrintPreference?.isEnabled = false
            fingerPrintPreference?.summary = "Your device doesn't support fingerprint biometrics"
        }
        pinPreference?.setOnPreferenceChangeListener { _, newValue ->
            val value = newValue as Boolean
            if(value){
                pinPreference?.isChecked = false
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToPinCodeFragment())
                return@setOnPreferenceChangeListener false
            }
            else{
                userDataStore.setPinEnable(false)
                changePinPreference?.isVisible = false
                fingerPrintPreference?.isVisible = false
                fingerPrintPreference?.isChecked = false
            }
            true
        }
    }
}