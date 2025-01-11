package io.github.aidenkoog.unittest_restapi_websocket.model.factory

import io.github.aidenkoog.unittest_restapi_websocket.model.config.CmdType
import io.github.aidenkoog.unittest_restapi_websocket.model.config.Config.ARIS_ID
import io.github.aidenkoog.unittest_restapi_websocket.model.config.EventType
import io.github.aidenkoog.unittest_restapi_websocket.model.config.OptType
import io.github.aidenkoog.unittest_restapi_websocket.model.config.ResType
import io.github.aidenkoog.unittest_restapi_websocket.model.request.ServerSendDTO
import io.github.aidenkoog.unittest_restapi_websocket.model.response.CommonReqDTO

object MotionControlRequestFactory {
    fun get(optType: OptType, resType: ResType): ServerSendDTO = ServerSendDTO(
        event = EventType.FROM_CLIENT.name, data = CommonReqDTO(
            cmd = CmdType.MOTION.name,
            aris_id = ARIS_ID,
            opt = optType.name,
            res = resType.name
        )
    )
}