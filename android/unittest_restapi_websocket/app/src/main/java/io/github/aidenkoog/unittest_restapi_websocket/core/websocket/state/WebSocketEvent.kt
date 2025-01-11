package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state

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
    data object Idle : WebSocketEvent()
}