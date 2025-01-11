package io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library

import android.app.Application
import android.content.Context
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.EventProcessor
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.IMapper
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.NetworkConnectivityService
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.StateManager
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.core.WebSocket
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.cache.CacheController
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.connection.AppConnectionProvider
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.coroutine.CoroutineScope.scope
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.event.EventMapper
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.event.WebSocketEventProcessor
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.event.converter.ConverterType
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.internal.EventProvider
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.internal.EventStateManager
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.internal.ServiceExecutor
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.network.NetworkConnectivityServiceImpl
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.websocket.Receive
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.library.websocket.Send
import io.github.aidenkoog.unittest_restapi_websocket.core.websocket.state.WebSocketEvent
import kotlinx.coroutines.CoroutineScope
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

class WebSocketController private constructor(
    private val webSocketCore: WebSocket.Factory,
    serviceExecutor: ServiceExecutor,
    private val scope: CoroutineScope
) {

    inline fun <reified T : Any> create(): T = create(T::class.java)

    fun <T> create(service: Class<T>): T = implementService(service)

    private fun <T> implementService(serviceInterface: Class<T>): T {
        val proxy = Proxy.newProxyInstance(
            serviceInterface.classLoader, arrayOf(serviceInterface), invocationHandler
        )
        return serviceInterface.cast(proxy)
    }

    private val invocationHandler = InvocationHandler { _, method, nullableArgs ->
        method.annotations.getOrNull(0)?.let { annotation ->
            val args = nullableArgs ?: arrayOf()
            return@InvocationHandler when (annotation) {
                is Send -> serviceExecutor.executeSend(method, args)
                is Receive -> serviceExecutor.executeReceive(method, args)
                else -> require(false) { "there is no matching annotation" }
            }
        }
    }

    class Builder {
        private var webSocketCore: WebSocket.Factory? = null
        private val appConnectionProvider by lazy { AppConnectionProvider() }
        private var stateManager: StateManager? = null
        private var iMapperFactory: IMapper.Factory? = null
        private var context: Context? = null
        private var converterType: ConverterType = ConverterType.Serialization

        fun setWebSocketFactory(core: WebSocket.Factory): Builder =
            apply { this.webSocketCore = core }

        fun setApplicationContext(context: Context): Builder = apply {
            this.context = context
            (this.context as Application).registerActivityLifecycleCallbacks(appConnectionProvider)
        }

        fun setConverterType(type: ConverterType) = apply {
            converterType = type
        }

        fun setStateManager(manager: StateManager.Factory) = apply {
            stateManager = manager.create(
                connectionListener = appConnectionProvider,
                networkStatus = createNetworkConnectivity(),
                cacheController = createCacheController(),
                webSocketCore = checkNotNull(webSocketCore)
            )
        }

        fun setEventMapper(mapper: IMapper.Factory) = apply {
            iMapperFactory = mapper
        }

        private fun createCacheController(): CacheController {
            return CacheController.Factory().create()
        }

        private fun createNetworkConnectivity(): NetworkConnectivityService {
            require(context != null) { "Application Context should be set before request build()" }
            return NetworkConnectivityServiceImpl(checkNotNull(context))
        }


        fun build(): WebSocketController {
            return WebSocketController(
                webSocketCore = checkNotNull(webSocketCore),
                serviceExecutor = createServiceExecutor(),
                scope = scope
            )
        }

        private fun createServiceExecutor(): ServiceExecutor {
            checkEventMapperFactory()
            return ServiceExecutor.Factory(
                eventProvider = createEventProvider(),
                converterType = converterType,
                iMapperFactory = requireNotNull(iMapperFactory),
                scope = scope
            ).create()
        }

        private fun checkEventMapperFactory() {
            if (iMapperFactory == null) {
                iMapperFactory = EventMapper.Factory()
            }
        }

        private fun createEventProvider(): EventProvider {
            checkStateManager()
            return EventProvider.Factory(
                stateManager = checkNotNull(stateManager), eventProcessor = createEventProcessor()
            ).create()
        }

        private fun createEventProcessor(): EventProcessor<WebSocketEvent> {
            return WebSocketEventProcessor()
        }

        private fun checkStateManager() {
            if (stateManager == null) {
                createDefaultStateManager()
            }
        }

        private fun createDefaultStateManager() {
            stateManager = EventStateManager.Factory().create(
                connectionListener = appConnectionProvider,
                networkStatus = createNetworkConnectivity(),
                cacheController = createCacheController(),
                webSocketCore = checkNotNull(webSocketCore)
            )
        }
    }
}