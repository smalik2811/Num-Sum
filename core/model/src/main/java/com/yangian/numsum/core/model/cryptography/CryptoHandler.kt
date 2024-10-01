package com.yangian.numsum.core.model.cryptography

import java.security.SecureRandom
import javax.crypto.KeyGenerator

class CryptoHandler(
    private val algorithmName: String = "AES"
) {
    fun generateKey(keySize: Int = 256): String {
        val keyGenerator = KeyGenerator
            .getInstance(algorithmName)
        val secureRandom = SecureRandom()
        keyGenerator.init(keySize, secureRandom)
        val secretKey = keyGenerator.generateKey()
        val secretKeyByteArray = secretKey.encoded
        return byteArrayToHexString(secretKeyByteArray)
    }

    fun byteArrayToHexString(byteArray: ByteArray): String =
        byteArray.joinToString("") { String.format("%02x", it) }

    fun hexStringToByteArray(hexString: String): ByteArray = hexString.chunked(2)
        .map { it.toInt(16).toByte() }.toByteArray()

    fun getEncrypter(
        encryptionKey: ByteArray
    ): Encrypter {
        return Encrypter().apply {
            setAlgorithmName(algorithmName)
            setEncryptionKey(encryptionKey)
            build()
        }
    }

    fun getDecrypter(
        decryptionKey: ByteArray
    ): Decrypter {
        return Decrypter().apply {
            setAlgorithmName(algorithmName)
            setDecryptionKey(decryptionKey)
            build()
        }
    }
}