package io.github.aidenkoog.websocket.websocket_coroutine.state

sealed interface NetworkState {
    data object Available : NetworkState
    data object Unavailable : NetworkState
}