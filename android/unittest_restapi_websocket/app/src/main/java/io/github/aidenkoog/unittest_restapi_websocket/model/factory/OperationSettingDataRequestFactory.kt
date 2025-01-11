package io.github.aidenkoog.unittest_restapi_websocket.model.factory

import io.github.aidenkoog.unittest_restapi_websocket.model.config.CmdType
import io.github.aidenkoog.unittest_restapi_websocket.model.config.Config.ARIS_ID
import io.github.aidenkoog.unittest_restapi_websocket.model.config.EventType
import io.github.aidenkoog.unittest_restapi_websocket.model.config.OptType
import io.github.aidenkoog.unittest_restapi_websocket.model.config.ResType
import io.github.aidenkoog.unittest_restapi_websocket.model.request.ServerSendDTO
import io.github.aidenkoog.unittest_restapi_websocket.model.response.CommonReqDTO

object TestRequestFactory {
    fun get(
        cmdType: CmdType = CmdType.TEST_START, optType: OptType, resType: ResType
    ): ServerSendDTO = ServerSendDTO(
        event = EventType.FROM_CLIENT.name, data = CommonReqDTO(
            cmd = cmdType.name, aris_id = ARIS_ID, opt = optType.name, res = resType.name
        )
    )
}