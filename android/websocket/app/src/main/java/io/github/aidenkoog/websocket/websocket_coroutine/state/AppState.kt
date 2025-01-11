package io.github.aidenkoog.websocket.websocket_coroutine.state

sealed interface AppState

data object Active : AppState

data object Inactive : AppState