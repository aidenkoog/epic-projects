package io.github.aidenkoog.websocket.websocket_coroutine.core

interface Converter<T> {
    fun convert(data: String): T
}