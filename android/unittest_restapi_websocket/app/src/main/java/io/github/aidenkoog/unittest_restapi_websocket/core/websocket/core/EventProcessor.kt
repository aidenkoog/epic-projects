package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core

import kotlinx.coroutines.flow.Flow

interface EventProcessor<T> {
    fun collectEvent(): Flow<T>
    suspend fun onEventDelivery(event: T)
}