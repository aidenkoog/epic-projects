package io.github.aidenkoog.unittest_restapi_websocket

import android.util.Log
import io.github.aidenkoog.unittest_restapi_websocket.model.factory.ConnectionRequestFactory
import io.github.aidenkoog.unittest_restapi_websocket.model.factory.TurnOnSystemRequestFactory
import io.github.aidenkoog.unittest_restapi_websocket.ui.IOnShowLogListener
import io.github.aidenkoog.unittest_restapi_websocket.ui.TestInterface
import io.github.aidenkoog.unittest_restapi_websocket.utils.DebugLog

class TestCaseImpl : BaseTestCase() {

    private var onShowLogListener: IOnShowLogListener? = null
    fun setOnShowLogListener(listener: IOnShowLogListener?) {
        onShowLogListener = listener
    }

    private var viewModel: MainViewModel? = null

    fun setViewModel(mainViewModel: MainViewModel) {
        viewModel = mainViewModel
    }

    @TestInterface(name = "TURN ON SYSTEM", description = "System Control")
    private fun turnOnSystem() {
        DebugLog.i(TAG, "turnOnSystem")
        viewModel?.turnOnSystem(TurnOnSystemRequestFactory.get())
        onShowLogListener?.onLogMessage(Log.INFO, "turnOnSystem: ")
    }

    @TestInterface(name = "CONNECTION", description = "Connection Notify")
    private fun connectionEvent() {
        DebugLog.i(TAG, "connectionEvent")
        viewModel?.notifyConnectionState(ConnectionRequestFactory.get())
        onShowLogListener?.onLogMessage(Log.INFO, "connectionEvent: ")
    }

    companion object {
        private val TAG = TestCaseImpl::class.java.simpleName
    }
}
