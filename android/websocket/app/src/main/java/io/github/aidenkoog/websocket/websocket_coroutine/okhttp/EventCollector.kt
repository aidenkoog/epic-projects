package io.github.aidenkoog.websocket.websocket_coroutine.okhttp

import io.github.aidenkoog.websocket.websocket_coroutine.state.WebSocketEvent
import kotlinx.coroutines.flow.Flow

interface EventCollector {
    fun collectEvent(): Flow<WebSocketEvent>
}