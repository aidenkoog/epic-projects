package io.github.aidenkoog.websocket.websocket_coroutine.state

sealed interface GlobalError {
    data object General : GlobalError
    data object NetworkLoss : GlobalError

    data class SocketLoss(
        val message: String?
    ) : GlobalError
}

