package io.github.aidenkoog.websocket.websocket_coroutine.library.cache

import io.github.aidenkoog.websocket.websocket_coroutine.core.cache.BaseRecovery
import io.github.aidenkoog.websocket.websocket_coroutine.core.event.EventRequest


class RecoveryCache : BaseRecovery<EventRequest> {

    private var latestRequest: EventRequest? = null

    override fun set(value: EventRequest) {
        latestRequest = value
    }

    override fun get(): EventRequest? {
        return latestRequest
    }

    override fun hasCache(): Boolean = latestRequest != null

    override fun clear() {
        latestRequest = null
    }

    class Factory : BaseRecovery.BaseRecoveryFactory<EventRequest> {

        override fun create(): RecoveryCache {
            return RecoveryCache()
        }
    }
}