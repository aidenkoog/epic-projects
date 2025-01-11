package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.cache

import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.ConnectState
import kotlinx.coroutines.flow.Flow

interface BaseValve<T> {

    fun onUpdateValve(state: ConnectState)
    fun requestToValve(request: T)
    fun emissionOfValveFlow(): Flow<List<T>>

    interface BaseValveFactory<T> {
        fun create(): BaseValve<T>
    }
}