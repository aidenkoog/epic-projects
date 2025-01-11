package io.github.aidenkoog.websocket.websocket_coroutine.library.cache

import io.github.aidenkoog.websocket.websocket_coroutine.core.cache.BaseRecovery
import io.github.aidenkoog.websocket.websocket_coroutine.core.cache.BaseValve
import io.github.aidenkoog.websocket.websocket_coroutine.core.cache.ICacheController
import io.github.aidenkoog.websocket.websocket_coroutine.core.event.EventRequest


class CacheController(
    private val recoveryCache: BaseRecovery<EventRequest>,
    private val valveCache: BaseValve<EventRequest>
) : ICacheController<EventRequest> {

    override fun getRecovery(): BaseRecovery<EventRequest> {
        return recoveryCache
    }

    override fun getValve(): BaseValve<EventRequest> {
        return valveCache
    }


    class Factory {
        private fun createRecoveryCache(): RecoveryCache {
            return RecoveryCache.Factory().create()
        }

        private fun createValveCache(): ValveCache {
            return ValveCache.Factory().create()
        }

        fun create(): CacheController {
            return CacheController(
                createRecoveryCache(), createValveCache()
            )
        }
    }
}