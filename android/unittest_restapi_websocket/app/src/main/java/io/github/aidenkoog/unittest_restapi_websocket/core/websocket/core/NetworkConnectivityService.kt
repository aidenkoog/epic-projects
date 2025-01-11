package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core

import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.NetworkState
import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityService {
    val networkStatus: Flow<NetworkState>
    fun hasAvailableNetworks(): Boolean
}