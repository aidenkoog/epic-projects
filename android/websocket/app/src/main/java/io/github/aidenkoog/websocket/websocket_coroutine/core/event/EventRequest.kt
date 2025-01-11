package io.github.aidenkoog.websocket.websocket_coroutine.core.event

enum class RequestType {
    GENERAL
}

sealed interface EventRequest {
    val typeOfRequest: RequestType
}

data class WebSocketRequest(
    val msg: String
) : EventRequest {
    override val typeOfRequest: RequestType = RequestType.GENERAL
}