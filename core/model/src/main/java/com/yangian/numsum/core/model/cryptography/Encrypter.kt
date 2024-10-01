package com.yangian.numsum.core.model.cryptography

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Encrypter {
    private lateinit var algorithmName: String
    private lateinit var encryptionKey: ByteArray
    private lateinit var secretKeySpec: SecretKeySpec
    private lateinit var cipher: Cipher

    fun setAlgorithmName(algorithmName: String) {
        this.algorithmName = algorithmName
    }

    fun setEncryptionKey(encryptionKey: ByteArray) {
        this.encryptionKey = encryptionKey
    }

    fun build(): Encrypter {
        secretKeySpec = SecretKeySpec(encryptionKey, algorithmName)
        cipher = Cipher.getInstance(algorithmName)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        return this
    }

    fun encryptString(plainText: String): String {
        val encryptedString = cipher.doFinal(plainText.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedString)
    }

}