package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.event

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