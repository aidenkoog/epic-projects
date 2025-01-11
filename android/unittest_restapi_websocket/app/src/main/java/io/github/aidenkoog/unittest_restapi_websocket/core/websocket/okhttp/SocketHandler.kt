package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.okhttp

import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.WebSocket
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.webSocketLog
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.WebSocketEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class SocketHandler : WebSocket {

    private var socket: okhttp3.WebSocket? = null

    fun initWebSocket(socket: okhttp3.WebSocket) {
        this.socket = socket
    }

    override fun open(): Flow<WebSocketEvent> {
        return emptyFlow()
    }

    override fun send(data: String): Boolean {
        webSocketLog("send: $data")
        return socket?.send(data) ?: false
    }

    override fun close(code: Int, reason: String): Boolean {
        return socket?.close(code, reason).apply { socket = null } ?: false
    }

    override fun cancel() {
        socket?.cancel()
        socket = null
    }
}