package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core

import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.WebSocketEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface IMapper<T> {
    fun mapEventToGeneric(event: WebSocketEvent)

    fun mapEventFlow(): Flow<T>

    interface Factory {
        fun create(converter: Converter<*>, coroutineScope: CoroutineScope): IMapper<*>
    }
}