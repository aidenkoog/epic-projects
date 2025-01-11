package io.github.aidenkoog.websocket.websocket_coroutine.library.event

import io.github.aidenkoog.websocket.websocket_coroutine.core.EventProcessor
import io.github.aidenkoog.websocket.websocket_coroutine.state.WebSocketEvent
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