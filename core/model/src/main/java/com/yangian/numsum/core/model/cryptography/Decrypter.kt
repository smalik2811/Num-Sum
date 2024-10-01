package com.yangian.numsum.core.model.cryptography

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Decrypter {
    private lateinit var algorithmName: String
    private lateinit var decryptionKey: ByteArray
    private lateinit var secretKeySpec: SecretKeySpec
    private lateinit var cipher: Cipher

    fun setAlgorithmName(algorithmName: String) {
        this.algorithmName = algorithmName
    }

    fun setDecryptionKey(decryptionKey: ByteArray) {
        this.decryptionKey = decryptionKey
    }

    fun build(): Decrypter {
        secretKeySpec = SecretKeySpec(decryptionKey, algorithmName)
        cipher = Cipher.getInstance(algorithmName)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
        return this
    }

    fun decryptString(encryptedText: String): String {
        val decryptedString = cipher.doFinal(Base64.getDecoder().decode(encryptedText))
        return String(decryptedString)
    }
}