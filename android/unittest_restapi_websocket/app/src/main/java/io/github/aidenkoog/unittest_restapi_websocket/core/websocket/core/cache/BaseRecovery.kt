package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.cache

interface BaseRecovery<T> {
    fun set(value: T)
    fun get(): T?

    fun hasCache(): Boolean
    fun clear()

    interface BaseRecoveryFactory<T> {
        fun create(): BaseRecovery<T>
    }
}