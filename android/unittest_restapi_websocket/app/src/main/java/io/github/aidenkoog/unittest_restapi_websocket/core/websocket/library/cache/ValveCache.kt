package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.cache

import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.cache.BaseValve
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.event.EventRequest
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.ConnectState
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import java.util.concurrent.ConcurrentLinkedQueue

class ValveCache : BaseValve<EventRequest> {
    private val isEmissiable = MutableStateFlow(true)

    private val innerQueue = ConcurrentLinkedQueue<EventRequest>()

    private fun cacheFlow(): Flow<List<EventRequest>> = flow {
        while (currentCoroutineContext().isActive) {
            if (isEmissiable.value && innerQueue.isNotEmpty()) {
                val emitCacheList = mutableListOf<EventRequest>()
                while (innerQueue.isNotEmpty()) {
                    innerQueue.poll()?.let { emitCacheList.add(it) }
                }
                emit(emitCacheList)
            }
            delay(CACHE_EMIT_GAP)
        }
    }

    override fun onUpdateValve(state: ConnectState) {
        isEmissiable.update { state is ConnectState.Establish }
    }

    override fun requestToValve(request: EventRequest) {
        val lastRequest = innerQueue.poll()
        if (lastRequest != request) {
            innerQueue.add(request)
        }
    }

    override fun emissionOfValveFlow(): Flow<List<EventRequest>> = cacheFlow()

    companion object {
        private const val CACHE_EMIT_GAP = 500L
    }

    class Factory : BaseValve.BaseValveFactory<EventRequest> {
        override fun create(): ValveCache {
            return ValveCache()
        }
    }
}