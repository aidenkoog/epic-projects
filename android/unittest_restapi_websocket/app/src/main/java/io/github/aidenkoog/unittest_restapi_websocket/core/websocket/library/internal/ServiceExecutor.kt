package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.internal

import com.google.gson.Gson
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.Converter
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.IMapper
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.event.WebSocketRequest
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.event.SocketEventKeyStore
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.event.converter.ConverterType
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.event.converter.GsonConvertAdapter
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.event.converter.SerializeConvertAdapter
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.getAboutRawType
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.getParameterUpperBound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ServiceExecutor internal constructor(
    private val eventProvider: EventProvider,
    private val converterType: ConverterType,
    private val iMapperFactory: IMapper.Factory,
    private val scope: CoroutineScope
) {

    fun executeReceive(method: Method, args: Array<out Any>): Any {
        require(method.genericReturnType != Flow::class.java) { "return type must flow" }
        return when (method.genericReturnType.getAboutRawType()) {
            Flow::class.java -> {

                method.requireParameterTypes { "Receive method must have zero parameter: $method" }
                method.requireReturnTypeIsOneOf(ParameterizedType::class.java) { "Receive method must return ParameterizedType: $method" }

                val returnType =
                    (method.genericReturnType as ParameterizedType).getParameterUpperBound(0)
                val converter = checkConverterType(converterType, returnType)
                val eventMapper = SocketEventKeyStore().findEventMapper(
                    returnType, method.annotations, converter, scope, iMapperFactory
                )

                eventProvider.observeEvent().onEach { eventMapper.mapEventToGeneric(it) }
                    .launchIn(scope)
                eventMapper.mapEventFlow().filterNotNull().createPipeline().receiveFlow()
            }

            else -> require(false) { "Wrapper Type must be Flow." }
        }
    }

    private fun checkConverterType(
        converterType: ConverterType, returnType: Type
    ): Converter<out Any?> {
        return when (converterType) {
            ConverterType.Gson -> GsonConvertAdapter.Factory().create(returnType)
            ConverterType.Serialization -> SerializeConvertAdapter.Factory().create(returnType)
        }
    }

    private fun Flow<Any>.createPipeline(): ReceivePipeline<*> = ReceivePipeline(this, scope)

    fun executeSend(method: Method, args: Array<out Any>) {
        scope.launch(Dispatchers.Default) {
            val request = Gson().toJson(args[0])
            eventProvider.send(WebSocketRequest(request))
        }
    }

    class Factory(
        private val eventProvider: EventProvider,
        private val converterType: ConverterType,
        private val iMapperFactory: IMapper.Factory,
        private val scope: CoroutineScope,
    ) {

        fun create(): ServiceExecutor {
            return ServiceExecutor(eventProvider, converterType, iMapperFactory, scope)
        }
    }

    companion object {
        private inline fun Method.requireParameterTypes(
            vararg types: Class<*>, lazyMessage: () -> Any
        ) {
            require(genericParameterTypes.size == types.size, lazyMessage)
            require(
                genericParameterTypes.zip(types).all { (t1, t2) -> t2 === t1 || t2.isInstance(t1) },
                lazyMessage
            )
        }

        private inline fun Method.requireReturnTypeIsOneOf(
            vararg types: Class<*>, lazyMessage: () -> Any
        ) = require(
            types.any { it === genericReturnType || it.isInstance(genericReturnType) }, lazyMessage
        )
    }
}