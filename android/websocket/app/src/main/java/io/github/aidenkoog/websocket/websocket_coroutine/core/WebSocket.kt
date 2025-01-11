package io.github.aidenkoog.websocket.websocket_coroutine.core

import io.github.aidenkoog.websocket.websocket_coroutine.state.WebSocketEvent
import kotlinx.coroutines.flow.Flow

interface WebSocket {

    fun open(): Flow<WebSocketEvent>
    fun send(data: String): Boolean
    fun close(code: Int, reason: String): Boolean
    fun cancel()

    interface Factory {
        fun create(): WebSocket
    }
}