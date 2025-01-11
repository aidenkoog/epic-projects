package io.github.aidenkoog.usecase.usecase.base

sealed class CustomResult<out T> {
    data class Success<out T>(val value: T) : CustomResult<T>()
    data class Failure(val exception: Exception) : CustomResult<Nothing>()
}