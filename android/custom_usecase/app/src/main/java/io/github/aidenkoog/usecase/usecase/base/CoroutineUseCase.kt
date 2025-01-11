package io.github.aidenkoog.usecase.usecase.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

interface CoroutineUseCase {
    /**
     * handle block function without using coroutine dispatcher.
     */
    suspend operator fun <T> invoke(block: suspend () -> T) = supervisorScope {
        try {
            CustomResult.Success(block())

        } catch (e: Exception) {
            CustomResult.Failure(e)
        }
    }

    /**
     * handle block function using coroutine dispatcher.
     * @return CustomResult<T>, T means Data<M>, ex. Data<ApprovePaymentResponse>
     */
    suspend operator fun <T> invoke(
        dispatcher: CoroutineDispatcher,
        block: suspend () -> T,
    ): CustomResult<T> = supervisorScope {
        try {
            CustomResult.Success(withContext(dispatcher) { block() })

        } catch (e: Exception) {
            CustomResult.Failure(e)
        }
    }
}