package io.github.aidenkoog.websocket.websocket.request.feature.test

import io.github.aidenkoog.websocket.websocket.request.command.CommandType
import io.github.aidenkoog.websocket.websocket.request.message.WebSocketMessage
import io.github.aidenkoog.websocket.websocket.request.type.EventType
import io.github.aidenkoog.websocket.websocket.request.type.OptionType
import io.github.aidenkoog.websocket.websocket.request.type.ResponseType

object SystemOnRequest {

    fun getSystemOnMessage(deviceId: String): WebSocketMessage {
        return WebSocketMessage(
            event = EventType.OPERATION.name,
            data = getRequestData(deviceId = deviceId),
        )
    }

    private fun getRequestData(deviceId: String): WebSocketMessage.Request =
        WebSocketMessage.Request(
            cmd = CommandType.TEST_START.name,
            deviceId = deviceId,
            opt = OptionType.SYSTEM_ON.name,
            res = ResponseType.SUCCESS.name,
        )
}