package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state

sealed interface AppState

data object Active : AppState

data object Inactive : AppState