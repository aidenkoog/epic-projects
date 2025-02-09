import asyncio
from services.serial_service import serial_service
from services.modbus_service import modbus_service
from services.tcp_socket import check_device_status

class StateManager:
    def __init__(self):
        """ 시스템 상태 초기화 """
        self.state = {
            "modbus": False,
            "robot": False,
            "serial": False,
            "pcb_event": "",
            "modbus_event": "",
            "robot_event": ""
        }

    async def update_state(self):
        while True:
            try:
                self.state["pcb_event"] = serial_service.read_data()

                self.state["modbus_event"] = modbus_service.read_status(5)

                self.state["device_event"] = await check_device_status()

                self.state["modbus"] = self.state["modbus_event"] is not None
                self.state["device"] = self.state["device_event"] is not None
                self.state["serial"] = self.state["pcb_event"] is not None

            except Exception as e:
                print(f"상태 업데이트 오류: {e}")

            await asyncio.sleep(1)

    def get_current_state(self):
        return self.state

state_manager = StateManager()
