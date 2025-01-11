package io.github.aidenkoog.unittest_restapi_websocket.ui

@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestInterface(
    val name: String = "", val description: String = "", val runOnBackground: Boolean = false,
)
