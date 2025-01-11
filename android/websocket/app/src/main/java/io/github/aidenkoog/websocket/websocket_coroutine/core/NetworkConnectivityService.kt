package io.github.aidenkoog.websocket.websocket_coroutine.core

import io.github.aidenkoog.websocket.websocket_coroutine.state.NetworkState
import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityService {
    val networkStatus: Flow<NetworkState>
    fun hasAvailableNetworks(): Boolean
}