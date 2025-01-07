package com.example.antoanbmtt.helper

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PINStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    companion object {
        private const val KEY_ALIAS = "pin_hash_key"
    }

    fun storePin(pin: String) {
        val pinHash = hashPin(pin)
        try {
            val secretKeyEntry = KeyStore.SecretKeyEntry(SecretKeySpec(pinHash, "HMACSHA256"))
            keyStore.setEntry(KEY_ALIAS, secretKeyEntry, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun comparePin(enteredPin: String): Boolean {
        val storedHash = getStoredPinHash()
        val enteredPinHash = hashPin(enteredPin)
        return storedHash.contentEquals(enteredPinHash)
    }

    private fun hashPin(pin: String): ByteArray {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.digest(pin.toByteArray())
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("SHA-256 algorithm not available", e)
        }
    }

    private fun getStoredPinHash(): ByteArray {
        try {
            val keyEntry = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
            keyEntry?.let {
                return it.secretKey.encoded
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return byteArrayOf()
    }
}
