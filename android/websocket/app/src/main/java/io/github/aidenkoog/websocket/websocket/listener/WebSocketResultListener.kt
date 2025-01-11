package io.github.aidenkoog.websocket.websocket.listener

interface WebSocketResultListener {
    fun onConnectionTryError()
    fun onConnectionSuccess()
    fun onConnectionFailed()
    fun onReconnectionTimeExceeded()
    fun onClose()
    fun onMessage(message: String?)
}