from fastapi import APIRouter
from services.modbus_service import modbus_service
from services.tcp_socket import send_command

router = APIRouter()

@router.post("/on")
async def system_on():
    try:
        modbus_service.send_command(1, 1) 
        response = await send_command("SYSTEM_ON", "", 0)
        return {"status": "success", "message": response}
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
