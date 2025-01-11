package io.github.aidenkoog.websocket.websocket_coroutine.state

sealed class ConnectState {
    data object Initialize : ConnectState()

    data object Establish : ConnectState()

    data class ConnectError(
        val typeOfError: GlobalError
    ) : ConnectState()

    data class ConnectClose(
        val reason: String? = null
    ) : ConnectState()
}

