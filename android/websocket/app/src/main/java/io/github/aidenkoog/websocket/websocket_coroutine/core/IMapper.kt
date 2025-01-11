package io.github.aidenkoog.websocket.websocket_coroutine.core

import io.github.aidenkoog.websocket.websocket_coroutine.state.WebSocketEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface IMapper<T> {
    fun mapEventToGeneric(event: WebSocketEvent)

    fun mapEventFlow(): Flow<T>

    interface Factory {
        fun create(converter: Converter<*>, coroutineScope: CoroutineScope): IMapper<*>
    }
}