package io.github.aidenkoog.usecase.usecase.base

import java.net.UnknownHostException

class Error<T>(val result: CustomResult<Data<T>>) {
    private var hasBeenHandled = false

    /**
     * check if error has been handled before invoking block function.
     */
    suspend fun invokeIfNotHandled(block: suspend () -> Unit) {
        if (hasBeenHandled.not()) {
            hasBeenHandled = true
            kotlin.runCatching { block() }
        }
    }
}

/**
 * return response data.
 * onResponse returns Error<T> for handling error exception.
 * refs. it is impossible to call it from another higher-order function without the [cross-inline] keyword.
 */
suspend inline fun <T> CustomResult<Data<T>>.onResponse(crossinline block: suspend (T?) -> Unit): Error<T> {
    if (this is CustomResult.Success && this.value.isSuccess()) {
        kotlin.runCatching { block(this.value.data) }
    }
    return Error(this)
}

/**
 * return error message and code together to caller.
 */
suspend inline fun <T> Error<T>.onError(crossinline block: suspend (errorCode: String?, errorMessage: String) -> Unit) =
    apply {
        if (result is CustomResult.Success && result.value.isSuccess().not()) {
            invokeIfNotHandled {
                block(result.value.code, result.value.message ?: "empty message")
            }
        }
    }

/**
 * return error message corresponding to error code to caller.
 */
suspend inline fun <T> Error<T>.onError(
    vararg errorCode: String?,
    crossinline block: suspend (errorMessage: String) -> Unit,
) = apply {
    if (result is CustomResult.Success && result.value.isSuccess().not() && errorCode.any {
            it == result.value.code
        }) {
        invokeIfNotHandled {
            block(result.value.message ?: "empty message")
        }
    }
}

suspend inline fun <T> Error<T>.onFailure(crossinline block: suspend (exception: Exception) -> Unit) =
    apply {
        if (result is CustomResult.Failure) {
            invokeIfNotHandled {
                block(result.exception)
            }
        }
    }

/**
 * function to handle global errors such as token expiration, unknown exception etc.
 */
suspend inline fun <T> Error<T>.handleError(crossinline block: suspend (message: String) -> Unit) =
    apply {
        onError("...") {
            block("no response")

        }.onError("...") {
            block("mandatory input error")

        }.onError("...") {
            block("network system error")

        }.onError("...") {
            block("unknown error (9999)")

        }.onFailure {
            block(
                when (it) {
                    is UnknownHostException -> "UnknownHostException"
                    else -> "UnexpectedException"
                }
            )
        }
    }