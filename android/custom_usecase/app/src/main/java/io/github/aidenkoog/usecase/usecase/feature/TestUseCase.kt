package io.github.aidenkoog.usecase.usecase.feature

import io.github.aidenkoog.usecase.usecase.base.CoroutineUseCase
import io.github.aidenkoog.usecase.usecase.base.CustomResultUseCase
import io.github.aidenkoog.usecase.usecase.base.Data
import io.github.aidenkoog.usecase.usecase.base.UseCaseDispatcher.IoDispatcher

class TestUseCase(
    private val repository: TestRepository,
) : CoroutineUseCase, CustomResultUseCase<Data<ResponseModel>, RequestModel>() {
    override suspend fun invoke(params: RequestModel) = invoke(IoDispatcher) {
        repository.testRepository(params)
    }
}