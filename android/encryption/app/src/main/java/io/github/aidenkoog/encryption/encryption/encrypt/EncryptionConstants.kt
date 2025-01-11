package io.github.aidenkoog.encryption.encryption.encrypt

object EncryptionConstants {
    const val ALGORITHM = "AES"
    const val H_MAC_ALGORITHM = "HmacSHA256"
    const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding"

    val IV_BYTES = byteArrayOf(
        0x08, 0x09, 0x00, 0x09, 0x00, 0x00,
        0x08, 0x09, 0x00, 0x09, 0x00, 0x00,
        0x08, 0x09, 0x00, 0x09
    )

    const val DIGITS = "0123456789ABCDEF"
    const val CHARSET_NAME = "UTF-8"
}