package io.github.aidenkoog.websocket.websocket_coroutine.core.cache

import io.github.aidenkoog.websocket.websocket_coroutine.state.ConnectState
import kotlinx.coroutines.flow.Flow

interface BaseValve<T> {

    fun onUpdateValve(state: ConnectState)
    fun requestToValve(request: T)
    fun emissionOfValveFlow(): Flow<List<T>>

    interface BaseValveFactory<T> {
        fun create(): BaseValve<T>
    }
}