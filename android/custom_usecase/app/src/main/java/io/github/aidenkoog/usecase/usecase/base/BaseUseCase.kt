package io.github.aidenkoog.usecase.usecase.base

/**
 * base use case including abstract operator invoke.
 * refs.
 * unlike regular classes, generic types cannot form inheritance relationships.
 * thus keyword "out" has to be used.
 *
 * it's impossible to write parameters without keyword, "in"
 */
abstract class UseCase<out Type, in Params> where Type : Any {
    abstract suspend operator fun invoke(params: Params): Type?
}

abstract class CustomResultUseCase<out DataType, in Param> :
    UseCase<CustomResult<DataType>, Param>()