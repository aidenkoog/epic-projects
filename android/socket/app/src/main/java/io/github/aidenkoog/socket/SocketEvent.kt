package io.github.aidenkoog.socket

sealed class SocketEvent {
    data object Idle : SocketEvent()
    data class SendSocketEvent(val message: String) : SocketEvent()
    data class ObservedSocketEvent(val message: String) : SocketEvent()
    data object CloseSocketEvent : SocketEvent()
    data object OpenSocketEvent : SocketEvent()
    data class SocketFailureEvent(val errorMessage: String) : SocketEvent()
}