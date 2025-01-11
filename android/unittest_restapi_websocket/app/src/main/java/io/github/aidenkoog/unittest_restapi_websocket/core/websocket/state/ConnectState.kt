package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state

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

