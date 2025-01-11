package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state

sealed interface GlobalError {
    data object General : GlobalError
    data object NetworkLoss : GlobalError

    data class SocketLoss(
        val message: String?
    ) : GlobalError
}

