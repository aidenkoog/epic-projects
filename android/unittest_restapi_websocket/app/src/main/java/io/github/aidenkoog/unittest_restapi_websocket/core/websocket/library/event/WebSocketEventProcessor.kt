package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.event

import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.EventProcessor
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.WebSocketEvent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class WebSocketEventProcessor : EventProcessor<WebSocketEvent> {

    private val channel =
        Channel<WebSocketEvent>(capacity = 100, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun collectEvent(): Flow<WebSocketEvent> {
        return channel.receiveAsFlow()
    }

    override suspend fun onEventDelivery(event: WebSocketEvent) {
        channel.trySendBlocking(event)
    }
}