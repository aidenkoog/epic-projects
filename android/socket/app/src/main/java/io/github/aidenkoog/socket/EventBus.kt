package io.github.aidenkoog.socket

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

object EventBus {
    /*
     * replay: set previous data count to be delivered when collector is connected.
     * 0 case -> it can be delivered after the collect.
     * 1 case -> it starts by receiving the data just before collect.
     */
    private val events = MutableSharedFlow<Any>(replay = 0)
    val mutableEvents = events.asSharedFlow()

    suspend fun post(event: Any) = events.emit(event)

    inline fun <reified T> subscribe(): Flow<T> {
        return mutableEvents.filter { it is T }.map { it as T }
    }
}
