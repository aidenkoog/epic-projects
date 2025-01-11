package io.github.aidenkoog.websocket.websocket_coroutine.core

import io.github.aidenkoog.websocket.websocket_coroutine.state.AppState
import kotlinx.coroutines.flow.Flow

interface AppConnectionListener {
    fun collectAppState(): Flow<AppState>
}