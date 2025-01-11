package io.github.aidenkoog.websocket.websocket_coroutine.library.event.converter

sealed class ConverterType {
    data object Gson : ConverterType()

    data object Serialization : ConverterType()
}