package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state

sealed interface NetworkState {
    data object Available : NetworkState
    data object Unavailable : NetworkState
}