package com.example.antoanbmtt.security

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyPairUtil @Inject constructor(){
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
    val publicKey: PublicKey
        get() = keyPair!!.public

    val privateKey: PrivateKey
        get() = keyPair!!.private
}
