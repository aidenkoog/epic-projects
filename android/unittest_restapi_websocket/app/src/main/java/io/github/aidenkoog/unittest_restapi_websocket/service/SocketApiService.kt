package io.github.aidenkoog.unittest_restapi_websocket.service

import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.websocket.Receive
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.websocket.Send
import kotlinx.coroutines.flow.Flow

interface SocketService {
    @Send
    fun turnOnSystem()

    @Receive
    fun collectEvents(): Flow<Any>
}