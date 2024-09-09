package com.yangian.numsum.core.model

import android.annotation.SuppressLint
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class CryptoHandler(
    private val key: String
) {
    private val algorithmName: String = "AES"
    @SuppressLint("GetInstance")
    private val cipher: Cipher = Cipher.getInstance(algorithmName)
    private val secretKeySpec = SecretKeySpec(key.substring(0, 16).toByteArray(), algorithmName)

    init {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
    }

    fun encrypt(plainText: String): String {
        val encryptedString = cipher.doFinal(plainText.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedString)
    }

}