package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core

import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.AppState
import kotlinx.coroutines.flow.Flow

interface AppConnectionListener {
    fun collectAppState(): Flow<AppState>
}