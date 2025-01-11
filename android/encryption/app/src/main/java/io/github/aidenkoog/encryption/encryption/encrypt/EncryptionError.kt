package io.github.aidenkoog.encryption.encryption.encrypt

sealed class EncryptionError {
    data object EncryptError : EncryptionError()
    data object DecryptError : EncryptionError()
    data object GetHMacError : EncryptionError()
    data object VerifyMacError : EncryptionError()
}

enum class EncryptionErrorCategory {
    ENCRYPT_ERROR, DECRYPT_ERROR, GET_H_MAC_ERROR, VERIFY_MAC_ERROR
}