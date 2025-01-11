package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core

interface Converter<T> {
    fun convert(data: String): T
}