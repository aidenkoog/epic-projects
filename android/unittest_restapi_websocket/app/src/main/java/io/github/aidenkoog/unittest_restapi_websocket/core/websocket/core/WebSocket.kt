package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core

import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.WebSocketEvent
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