package io.github.aidenkoog.websocket.websocket_coroutine.state

sealed class WebSocketEvent {
    data class OnConnectionOpen(
        val webSocket: Any
    ) : WebSocketEvent()

    data class OnMessageReceived(
        val data: String
    ) : WebSocketEvent()

    data class OnConnectionError(
        val error: String?
    ) : WebSocketEvent()

    data object OnConnectionClosed : WebSocketEvent()
}