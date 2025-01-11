package io.github.aidenkoog.websocket.websocket.request.message

import com.google.gson.annotations.SerializedName

data class WebSocketMessage(
    val event: String,
    val data: Request,
) {
    data class Request(
        val cmd: String,
        @SerializedName("device_id") val deviceId: String,
        val opt: Any,
        val res: String,
    )
}

data class WebSocketConnectionMessage(
    val event: String, val data: Data,
) {
    data class Data(
        val deviceId: String,
    )
}