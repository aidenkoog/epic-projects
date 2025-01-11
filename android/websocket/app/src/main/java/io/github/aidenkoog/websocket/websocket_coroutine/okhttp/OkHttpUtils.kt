package io.github.aidenkoog.websocket.websocket_coroutine.okhttp

import io.github.aidenkoog.websocket.websocket_coroutine.core.WebSocket
import okhttp3.OkHttpClient
import okhttp3.Request

fun OkHttpClient.makeWebSocketCore(url: String): WebSocket.Factory {
    return OkHttpWebSocket.Factory(
        provider = ConnectionProvider(this, url),
    )
}

interface SocketListenerProvider {
    fun provide(socketListener: SocketListener)
}

class ConnectionProvider(
    private val okHttpClient: OkHttpClient, private val url: String
) : SocketListenerProvider {

    override fun provide(socketListener: SocketListener) {
        okHttpClient.newWebSocket(Request.Builder().url(url).build(), socketListener)
    }
}