package io.github.aidenkoog.encryption.encryption.encrypt

import io.github.aidenkoog.encryption.encryption.encrypt.ApiEncryptionKey.API_ENCRYPTION_KEY_DEV
import io.github.aidenkoog.encryption.encryption.encrypt.EncryptionConstants.ALGORITHM
import io.github.aidenkoog.encryption.encryption.encrypt.EncryptionConstants.CHARSET_NAME
import io.github.aidenkoog.encryption.encryption.encrypt.EncryptionConstants.CIPHER_TRANSFORMATION
import io.github.aidenkoog.encryption.encryption.encrypt.EncryptionConstants.DIGITS
import io.github.aidenkoog.encryption.encryption.encrypt.EncryptionConstants.H_MAC_ALGORITHM
import io.github.aidenkoog.encryption.encryption.encrypt.EncryptionConstants.IV_BYTES
import java.io.ByteArrayOutputStream
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * encryption, decryption utility class.
 *
 * refs.
 * H-MAC (hash-based message authentication)
 * SHA-256 (a cryptographic hashing algorithm (function) widely used to verify message, file, or data integrity.
 * CBC (cipher block chaining)
 * AES (advanced encryption standard)
 */
object EncryptionUtil {

    /**
     * encrypt input string.
     * @param input json string that is converted from data class.
     * @param key encryption unique key.
     * @return hex styled string.
     */
    @Throws(Exception::class)
    fun encryptAesToHex(input: String?): String? {
        if (input.isNullOrEmpty()) {
            return null
        }

        val iv: AlgorithmParameterSpec = IvParameterSpec(IV_BYTES)
        val secretKeySpec = SecretKeySpec(API_ENCRYPTION_KEY_DEV.toByteArray(), ALGORITHM)
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv)

        val inputBytes = input.toByteArray(charset(CHARSET_NAME))
        val encryptedBytes = cipher.doFinal(inputBytes)
        return changeBytesToHex(encryptedBytes)
    }

    /**
     * decrypt input string.
     * @param input response json string.
     * @param key encryption unique key.
     * @return decrypted string.
     */
    @Throws(Exception::class)
    fun decryptAesFromHex(input: String?): String? {
        if (input.isNullOrEmpty()) {
            return null
        }

        val iv: AlgorithmParameterSpec = IvParameterSpec(IV_BYTES)
        val secretKeySpec = SecretKeySpec(API_ENCRYPTION_KEY_DEV.toByteArray(), ALGORITHM)
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv)

        return String(cipher.doFinal(changeHexToByte(input)))
    }

    /**
     * get hex styled mac information
     * @return hex styled string.
     *
     * refs.
     * used for message integrity and message authentication purposes.
     * allows you to determine whether the message is from the person you want (using key)
     * create a message hash value (MAC) using the shared key.
     * prevents message falsification (변조) and spoofing (위장).
     *
     * @param input json string that is converted from data class.
     * @param key encryption unique key.
     * @return hex styled string.
     */
    @Throws(Exception::class)
    fun getHMacSha256(input: String?): String? {
        if (input.isNullOrEmpty()) {
            return null
        }
        val keySpec = SecretKeySpec(API_ENCRYPTION_KEY_DEV.toByteArray(), H_MAC_ALGORITHM)
        val mac = Mac.getInstance(H_MAC_ALGORITHM)

        mac.init(keySpec)

        val inputBytes = input.toByteArray(charset(CHARSET_NAME))
        val encryptedBytes = mac.doFinal(inputBytes)
        return changeBytesToHex(encryptedBytes)
    }

    @Throws(Exception::class)
    fun verifyMac(data: String?, hMac: String): Boolean {
        val decryptedData = decryptAesFromHex(data)
        val checkHMac = getHMacSha256(decryptedData)

        return hMac == checkHMac
    }

    private fun changeHexToByte(hex: String): ByteArray? {
        val outputStream = ByteArrayOutputStream()
        var i = 0
        while (i < hex.length) {
            val b = hex.substring(i, i + 2).toInt(16)
            outputStream.write(b)
            i += 2
        }
        return outputStream.toByteArray()
    }

    /**
     * change byte array to hex string.
     * ex. byte array(10, 2, 15, 11) --> 0A020F0B
     * original code: Hex.encodeHexString(data)
     */
    private fun changeBytesToHex(byteArray: ByteArray?): String? {
        if (byteArray == null) {
            return null
        }
        val hexChars = CharArray(byteArray.size * 2)
        for (i in byteArray.indices) {
            val v = byteArray[i].toInt() and 0xff
            hexChars[i * 2] = DIGITS[v shr 4]
            hexChars[i * 2 + 1] = DIGITS[v and 0xf]
        }
        return String(hexChars)
    }
}
