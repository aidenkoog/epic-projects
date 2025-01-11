package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.okhttp

import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.WebSocketEvent
import kotlinx.coroutines.flow.Flow

interface EventCollector {
    fun collectEvent(): Flow<WebSocketEvent>
}