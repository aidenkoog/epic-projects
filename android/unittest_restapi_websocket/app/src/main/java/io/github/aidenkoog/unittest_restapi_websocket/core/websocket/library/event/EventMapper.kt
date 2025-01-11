package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.event

import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.Converter
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.IMapper
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.WebSocketEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

class EventMapper<T>(
    private val converter: Converter<T>,
    coroutineScope: CoroutineScope
) : IMapper<T> {
    private val _eventMappingChannel = MutableSharedFlow<WebSocketEvent>(
        replay = 1,
        extraBufferCapacity = 100,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val mapToResultChannel = MutableSharedFlow<T>(
        replay = 1,
        extraBufferCapacity = 100,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        _eventMappingChannel
            .filterIsInstance<WebSocketEvent.OnMessageReceived>()
            .map { it.data }
            .map(converter::convert)
            .map(mapToResultChannel::tryEmit)
            .launchIn(coroutineScope)
    }

    override fun mapEventToGeneric(event: WebSocketEvent) {
        _eventMappingChannel.tryEmit(event)
    }

    override fun mapEventFlow(): Flow<T> = mapToResultChannel

    class Factory : IMapper.Factory {
        override fun create(
            converter: Converter<*>,
            coroutineScope: CoroutineScope
        ): EventMapper<*> {
            return EventMapper(converter, coroutineScope)
        }
    }
}