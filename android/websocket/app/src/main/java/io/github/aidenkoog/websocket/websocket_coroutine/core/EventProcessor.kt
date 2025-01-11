package io.github.aidenkoog.websocket.websocket_coroutine.core

import kotlinx.coroutines.flow.Flow

interface EventProcessor<T> {
    fun collectEvent(): Flow<T>
    suspend fun onEventDelivery(event: T)
}