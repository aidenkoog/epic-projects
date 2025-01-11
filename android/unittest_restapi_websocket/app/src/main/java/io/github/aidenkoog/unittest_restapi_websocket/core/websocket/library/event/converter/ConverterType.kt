package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.event.converter

sealed class ConverterType {
    data object Gson : ConverterType()

    data object Serialization : ConverterType()
}