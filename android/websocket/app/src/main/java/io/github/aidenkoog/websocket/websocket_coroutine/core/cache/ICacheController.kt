package io.github.aidenkoog.websocket.websocket_coroutine.core.cache

interface ICacheController<T> {
    fun getRecovery(): BaseRecovery<T>
    fun getValve(): BaseValve<T>
}