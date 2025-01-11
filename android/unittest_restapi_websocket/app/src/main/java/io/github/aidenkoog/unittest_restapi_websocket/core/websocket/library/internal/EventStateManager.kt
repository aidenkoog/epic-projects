package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.internal

import android.util.Log
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.AppConnectionListener
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.NetworkConnectivityService
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.StateManager
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.WebSocket
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.cache.BaseRecovery
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.cache.BaseValve
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.cache.ICacheController
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.event.EventRequest
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.event.WebSocketRequest
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.stateDelegate
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.coroutine.CoroutineScope.scope
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.webSocketLog
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.Active
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.ConnectState
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.GeneralManager
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.ManagerState
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.NetworkState
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.WebSocketEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext

class EventStateManager private constructor(
    private val connectionListener: AppConnectionListener,
    private val networkState: NetworkConnectivityService,
    private val recoveryCache: BaseRecovery<EventRequest>,
    private val valveCache: BaseValve<EventRequest>,
    private val webSocketCore: WebSocket.Factory,
    private val scope: CoroutineScope
) : StateManager {
    private val innerScope = scope + CoroutineExceptionHandler { _, throwable ->
        webSocketLog("[EventStateManager] = ${throwable.message}")
    }

    private var socket: WebSocket? = null

    private val _connectState = MutableStateFlow<ConnectState>(ConnectState.Initialize)

    private val _events = MutableSharedFlow<WebSocketEvent>(replay = 1)

    private val stateCollector: Flow<ConnectState> by stateDelegate(
        connectionListener.collectAppState(),
        networkState.networkStatus,
        _events,
        scope,
        ::recoveryProcess
    )

    override fun getStateOfType(): ManagerState = GeneralManager

    override fun collectWebSocketEvent() = _events.asSharedFlow()

    init {
        _connectState.map(valveCache::onUpdateValve).launchIn(innerScope)

        valveCache.emissionOfValveFlow().map { it.map(::requestSendMessage) }.launchIn(innerScope)

        stateCollector.onEach { state ->
            _connectState.update { state }
        }.onStart {
            openConnection {
                webSocketLog("Open First Connection.")
            }
        }.launchIn(innerScope)
    }

    private fun recoveryProcess() = innerScope.launch {
        connectionRecoveryProcess(onConnect = {
            requestRecoveryProcess()
        })
    }

    private suspend fun checkOnValidState(): Boolean = withContext(Dispatchers.Default) {
        val appState = connectionListener.collectAppState().firstOrNull()
        val networkState = networkState.networkStatus.firstOrNull()
        appState == Active && networkState == NetworkState.Available
    }

    private suspend fun connectionRecoveryProcess(onConnect: () -> Unit) {
        if (checkOnValidState()) {
            retryConnection(
                onConnect = onConnect,
            )
        }
    }

    private fun requestRecoveryProcess() {
        if (recoveryCache.hasCache()) {
            val request = recoveryCache.get()
            val webSocketRequest = (request as WebSocketRequest)
            valveCache.requestToValve(webSocketRequest)
            recoveryCache.clear()
        }
    }

    override fun retryConnection(onConnect: () -> Unit) {
        webSocketLog("retry connection work.")
        closeConnection {
            openConnection(onConnect)
        }
    }

    private fun requestSendMessage(message: EventRequest) = socket?.let {
        val msg = (message as WebSocketRequest).msg
        if (it.send(msg)) {
            recoveryCache.set(message)
        }
    }

    override fun openConnection(onConnect: () -> Unit) = synchronized(this) {
        if (socket == null) {
            socket = webSocketCore.create()
            socket?.open()?.onStart {
                onConnect.invoke()
                webSocketLog("Event open connection work start.")
            }?.onEach { _events.tryEmit(it) }?.launchIn(innerScope)
        }
    }

    override fun closeConnection(onDisconnect: () -> Unit): Unit = synchronized(this) {
        socket?.let {
            if (it.close(1000, "shutdown")) {
                webSocketLog("Event close connection work success.")
            } else {
                webSocketLog(
                    "Event close connection work failed because websocket already shutdown."
                )
            }
            socket = null
            onDisconnect.invoke()
        }
    }

    override fun send(message: EventRequest) {
        valveCache.requestToValve(message)
    }

    class Factory : StateManager.Factory {
        override fun create(
            connectionListener: AppConnectionListener,
            networkStatus: NetworkConnectivityService,
            cacheController: ICacheController<EventRequest>,
            webSocketCore: WebSocket.Factory
        ): StateManager {
            return EventStateManager(
                connectionListener = connectionListener,
                networkState = networkStatus,
                recoveryCache = cacheController.getRecovery(),
                valveCache = cacheController.getValve(),
                webSocketCore = webSocketCore,
                scope = scope
            )
        }
    }

    companion object {
        private const val RETRY_CONNECTION_GAP = 500L
    }
}