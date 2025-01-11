package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.cache

interface ICacheController<T> {
    fun getRecovery(): BaseRecovery<T>
    fun getValve(): BaseValve<T>
}