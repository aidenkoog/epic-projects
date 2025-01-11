package io.github.aidenkoog.websocket.websocket_coroutine.core

import io.github.aidenkoog.websocket.websocket_coroutine.core.cache.ICacheController
import io.github.aidenkoog.websocket.websocket_coroutine.core.event.EventRequest
import io.github.aidenkoog.websocket.websocket_coroutine.state.ManagerState
import io.github.aidenkoog.websocket.websocket_coroutine.state.WebSocketEvent
import kotlinx.coroutines.flow.Flow

interface StateManager {
    fun getStateOfType(): ManagerState

    fun collectWebSocketEvent(): Flow<WebSocketEvent>

    fun openConnection(onConnect: () -> Unit)

    fun closeConnection(onDisconnect: () -> Unit)

    fun retryConnection(onConnect: () -> Unit)

    fun send(message: EventRequest)

    interface Factory {
        fun create(
            connectionListener: AppConnectionListener,
            networkStatus: NetworkConnectivityService,
            cacheController: ICacheController<EventRequest>,
            webSocketCore: WebSocket.Factory
        ): StateManager
    }
}

