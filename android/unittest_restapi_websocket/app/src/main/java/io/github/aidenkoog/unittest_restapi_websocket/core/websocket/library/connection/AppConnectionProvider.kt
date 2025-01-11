package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.connection

import android.app.Activity
import android.app.Application
import android.os.Bundle
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.AppConnectionListener
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.Active
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.AppState
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.Inactive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AppConnectionProvider : AppConnectionListener, Application.ActivityLifecycleCallbacks {

    private val _eventFlow = MutableStateFlow<AppState>(Active)

    override fun collectAppState(): Flow<AppState> {
        return _eventFlow
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        _eventFlow.tryEmit(Active)
    }

    override fun onActivityStarted(activity: Activity) {
        _eventFlow.tryEmit(Active)
    }

    override fun onActivityResumed(activity: Activity) {
        _eventFlow.tryEmit(Active)
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        _eventFlow.tryEmit(Inactive)
    }
}