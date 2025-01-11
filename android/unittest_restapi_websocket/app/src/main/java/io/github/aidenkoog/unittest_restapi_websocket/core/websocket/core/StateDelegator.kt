package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core

import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.Active
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.AppState
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.ConnectState
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.GlobalError
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.Inactive
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.NetworkState
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.WebSocketEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlin.reflect.KProperty

fun stateDelegate(
    applicationStateFlow: Flow<AppState>,
    networkStateFlow: Flow<NetworkState>,
    webSocketEventFlow: Flow<WebSocketEvent>,
    scope: CoroutineScope,
    recovery: () -> Unit
) = StateDelegator(
    applicationStateFlow, networkStateFlow, webSocketEventFlow, scope, recovery
)

@OptIn(ExperimentalCoroutinesApi::class)
class StateDelegator(
    applicationStateFlow: Flow<AppState>,
    networkStateFlow: Flow<NetworkState>,
    webSocketEventFlow: Flow<WebSocketEvent>,
    scope: CoroutineScope,
    recovery: () -> Unit
) {

    init {
        applicationStateFlow.mapLatest { appState ->
            appState.checkAndRecovery {
                recovery.invoke()
            }
        }.combine(networkStateFlow) { upStreamState, networkState ->
            if (upStreamState) {
                networkState.checkAndRecovery {
                    recovery.invoke()
                }
            } else false
        }.combine(webSocketEventFlow) { upStreamState, webSocketEvent ->
            if (upStreamState) {
                when (webSocketEvent) {
                    is WebSocketEvent.OnMessageReceived, is WebSocketEvent.OnConnectionOpen -> {
                        _connectState.update { ConnectState.Establish }
                    }

                    WebSocketEvent.OnConnectionClosed -> {
                        _connectState.update { ConnectState.ConnectClose() }
                    }

                    is WebSocketEvent.OnConnectionError -> {
                        _connectState.update {
                            ConnectState.ConnectError(
                                GlobalError.SocketLoss(
                                    webSocketEvent.error
                                )
                            )
                        }
                        recovery.invoke()
                    }

                    is WebSocketEvent.Idle -> {}
                }
            }
        }.launchIn(scope)
    }

    private fun AppState.checkAndRecovery(recovery: () -> Unit): Boolean {
        return when (this) {
            Active -> true
            Inactive -> {
                recovery.invoke()
                false
            }
        }
    }

    private fun NetworkState.checkAndRecovery(recovery: () -> Unit): Boolean {
        return when (this) {
            NetworkState.Available -> true
            NetworkState.Unavailable -> {
                recovery.invoke()
                false
            }
        }
    }

    operator fun getValue(
        thisRef: Any?, property: KProperty<*>
    ): Flow<ConnectState> = _connectState

    private val _connectState = MutableStateFlow<ConnectState>(ConnectState.Initialize)

}