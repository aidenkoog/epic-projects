package io.github.aidenkoog.unittest

import android.util.Log
import io.github.aidenkoog.unittest.ui.IOnShowLogListener
import io.github.aidenkoog.unittest.ui.TestInterface
import io.github.aidenkoog.unittest.utils.DebugLog

class TestCaseImpl : BaseTestCase() {

    private var onShowLogListener: IOnShowLogListener? = null
    fun setOnShowLogListener(listener: IOnShowLogListener?) {
        onShowLogListener = listener
    }

    @TestInterface(
        name = "System Turn ON", description = "testcase description", runOnBackground = true
    )
    private fun turnOnSystem() {
        DebugLog.i(TAG, "turnOnSystem")
        onShowLogListener?.onLogMessage(Log.INFO, "turnOnSystem: ")
    }

    @TestInterface(name = "System Turn OFF", description = "testcase description")
    private fun turnOffSystem() {
        onShowLogListener?.onLogMessage(Log.INFO, "turnOffSystem: ")
    }

    @TestInterface(name = "System Reboot", description = "testcase description")
    private fun rebootSystem() {
        onShowLogListener?.onLogMessage(Log.INFO, "rebootSystem: ")
    }

    @TestInterface(name = "System Shutdown", description = "testcase description")
    private fun shutdownSystem() {
        onShowLogListener?.onLogMessage(Log.INFO, "shutdownSystem: ")
    }

    @TestInterface(name = "Show Device Info", description = "testcase description")
    private fun showDeviceInfo() {
        onShowLogListener?.onLogMessage(Log.INFO, "showDeviceInfo: ")
    }

    @TestInterface(name = "Show App Info", description = "testcase description")
    private fun showAppInfo() {
        onShowLogListener?.onLogMessage(Log.INFO, "showAppInfo: ")
    }

    @TestInterface(name = "TestCase 100", description = "testcase description")
    private fun testCase100() {
        onShowLogListener?.onLogMessage(Log.INFO, "testCase100: ")
    }

    @TestInterface(name = "TestCase 101", description = "testcase description")
    private fun testCase101() {
        onShowLogListener?.onLogMessage(Log.INFO, "testCase101: ")
    }

    @TestInterface(name = "TestCase 101", description = "testcase description")
    private fun testCase102() {
        onShowLogListener?.onLogMessage(Log.INFO, "testCase102: ")
    }

    companion object {
        private val TAG = TestCaseImpl::class.java.simpleName
    }
}
