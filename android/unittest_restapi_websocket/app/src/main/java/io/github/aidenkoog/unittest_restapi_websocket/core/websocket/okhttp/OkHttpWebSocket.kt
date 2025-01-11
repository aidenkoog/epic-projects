package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.okhttp

import io.github.aidenkoog.unittest_restapi_websocket.bridge.EventBus
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.WebSocket
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.WebSocketEvent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


class OkHttpWebSocket internal constructor(
    private val provider: ConnectionProvider,
    private val socketListener: SocketListener,
    private val socketHandler: SocketHandler,
) : WebSocket {

    private var scopeJob: Job? = null
    override fun open(): Flow<WebSocketEvent> = socketListener.collectEvent().onStart {
        provider.provide(socketListener)
    }.onEach {
        when (it) {
            is WebSocketEvent.OnConnectionOpen -> {
                socketHandler.initWebSocket(it.webSocket as okhttp3.WebSocket)
                notifyWebSocketEvent(it)
            }

            else -> notifyWebSocketEvent(it)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun notifyWebSocketEvent(event: WebSocketEvent) {
        scopeJob = GlobalScope.launch(Dispatchers.IO) {
            when (event) {
                is WebSocketEvent.OnConnectionOpen -> EventBus.post(
                    WebSocketEvent.OnConnectionOpen(event.webSocket)
                )

                is WebSocketEvent.OnConnectionError -> EventBus.post(
                    WebSocketEvent.OnConnectionError(event.error)
                )

                is WebSocketEvent.OnConnectionClosed -> EventBus.post(WebSocketEvent.OnConnectionClosed)

                is WebSocketEvent.OnMessageReceived -> EventBus.post(
                    WebSocketEvent.OnMessageReceived(event.data)
                )

                is WebSocketEvent.Idle -> EventBus.post(WebSocketEvent.Idle)
            }
        }
    }

    override fun send(data: String): Boolean {
        return socketHandler.send(data)
    }

    override fun close(code: Int, reason: String): Boolean {
        scopeJob?.cancel()
        return socketHandler.close(code, reason)
    }

    override fun cancel() {
        scopeJob?.cancel()
        socketHandler.cancel()
    }


    class Factory(
        private val provider: ConnectionProvider,
    ) : WebSocket.Factory {
        override fun create(): WebSocket = OkHttpWebSocket(
            provider = provider,
            socketListener = SocketListener(),
            socketHandler = SocketHandler(),
        )
    }
}