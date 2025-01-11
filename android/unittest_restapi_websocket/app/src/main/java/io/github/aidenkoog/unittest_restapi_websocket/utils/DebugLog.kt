package io.github.aidenkoog.unittest_restapi_websocket.utils

import android.util.Log

object DebugLog {
    private val TAG = DebugLog::class.java.simpleName
    private var debug = true

    fun init(debug: Boolean) {
        this.debug = debug
    }

    fun v(tag: String, log: String?) {
        if (debug) {
            Log.v("$TAG[$tag]", log!!)
        }
    }

    fun i(tag: String, log: String?) {
        if (debug) {
            Log.i("$TAG[$tag]", log!!)
        }
    }

    fun d(tag: String, log: String?) {
        if (debug) {
            Log.d("$TAG[$tag]", log!!)
        }
    }

    fun w(tag: String, log: String?) {
        Log.w("$TAG[$tag]", log!!)
    }

    fun e(tag: String, log: String?) {
        Log.e("$TAG[$tag]", log!!)
    }
}