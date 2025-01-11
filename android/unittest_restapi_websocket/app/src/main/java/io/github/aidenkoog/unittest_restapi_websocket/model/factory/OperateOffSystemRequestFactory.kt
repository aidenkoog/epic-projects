package io.github.aidenkoog.unittest_restapi_websocket.model.factory

import io.github.aidenkoog.unittest_restapi_websocket.model.config.CmdType
import io.github.aidenkoog.unittest_restapi_websocket.model.config.Config.ARIS_ID
import io.github.aidenkoog.unittest_restapi_websocket.model.config.EventType
import io.github.aidenkoog.unittest_restapi_websocket.model.config.OptType
import io.github.aidenkoog.unittest_restapi_websocket.model.request.ServerSendDTO
import io.github.aidenkoog.unittest_restapi_websocket.model.response.CommonReqDTO

object OperateOnSystemRequestFactory {
    fun get(): ServerSendDTO = ServerSendDTO(
        event = EventType.FROM_CLIENT.name, data = CommonReqDTO(
            cmd = CmdType.ON_OFF.name,
            aris_id = ARIS_ID,
            opt = OptType.OPERATE_ON.name,
            res = "SUCCESS"
        )
    )
}