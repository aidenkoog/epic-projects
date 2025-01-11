package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object CoroutineScope {
    internal val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
}