package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.internal

import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.EventProcessor
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.StateManager
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.event.EventRequest
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.coroutine.CoroutineScope.scope
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.WebSocketEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventProvider internal constructor(
    private val stateManager: StateManager,
    private val eventProcessor: EventProcessor<WebSocketEvent>,
    scope: CoroutineScope
) {

    init {
        stateManager.collectWebSocketEvent().onEach(eventProcessor::onEventDelivery).launchIn(scope)
    }

    fun observeEvent(): Flow<WebSocketEvent> = eventProcessor.collectEvent()

    fun send(request: EventRequest) {
        stateManager.send(request)
    }

    fun subscribe(request: EventRequest) {
        stateManager.send(request)
    }

    class Factory(
        private val stateManager: StateManager,
        private val eventProcessor: EventProcessor<WebSocketEvent>
    ) {

        fun create(): EventProvider {
            return EventProvider(stateManager, eventProcessor, scope)
        }
    }
}