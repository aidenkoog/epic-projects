from fastapi import APIRouter
from services.modbus_service import modbus_service
from services.tcp_socket import send_command, connect_socket

router = APIRouter()

@router.post("/on")
async def system_on():
    try:
        modbus_service.send_command(register=1, value=1)
        event_detected = await modbus_service.wait_for_event(register=5, target_value=100, timeout=10)
        if not event_detected:
            return {"status": "error", "message": "Modbus 이벤트 수신 실패 (Timeout)"}
        robot_response = await connect_socket()

        return {"status": "success", "message": f"로봇 연결 완료: {robot_response}"}
    except Exception as e:
        return {"status": "error", "message": str(e)}

@router.post("/off")
async def system_off():
    try:
        modbus_service.send_command(1, 0) 
        response = await send_command("SYSTEM_OFF", "", 0)
        return {"status": "success", "message": response}
    except Exception as e:
        return {"status": "error", "message": str(e)}
