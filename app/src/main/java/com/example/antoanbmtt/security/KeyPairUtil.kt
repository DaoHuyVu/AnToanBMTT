package com.example.antoanbmtt.security

import com.example.antoanbmtt.repository.UserDataStore
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyPairUtil @Inject constructor(
    private val userDataStore: UserDataStore
) {
    private var keyPair: KeyPair? = null

    init {
        try {
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(2048)
            keyPair = keyPairGenerator.generateKeyPair()
        } catch (e: Exception) {
            throw RuntimeException("Error generating key pair", e)
        }
    }

    val publicKey: String
        get() = Base64.getEncoder().encodeToString(keyPair!!.public.encoded)

    val privateKey: PrivateKey
        get() = keyPair!!.private

    fun encWithPublicKey(plainText: String): String {
        try {
            val publicKeyBytes = Base64.getDecoder().decode(userDataStore.getPublicKey())

            val keySpec = X509EncodedKeySpec(publicKeyBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            val publicKey: PublicKey = keyFactory.generatePublic(keySpec)

            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)

            val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

            return Base64.getEncoder().encodeToString(encryptedBytes)
        } catch (e: Exception) {
            throw RuntimeException("Error encrypting data", e)
        }
    }
}
