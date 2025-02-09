from pymodbus.client import ModbusTcpClient
from config import MODBUS_HOST, MODBUS_PORT

class ModbusService:
    def __init__(self):
        self.client = ModbusTcpClient(MODBUS_HOST, MODBUS_PORT)

    def send_command(self, register, value):
        self.client.write_register(register, value)

    def read_status(self, register):
        return self.client.read_holding_registers(register, 1).registers[0]
    
    async def wait_for_event(self, register, target_value, timeout=10):
        # 0.5초 간격 체크 (10초간 반복)
        for _ in range(timeout * 2):
            result = self.client.read_holding_registers(register, 1)
            if result and result.registers[0] == target_value:
                return True
            await asyncio.sleep(0.5)  # 0.5초 간격으로 감지
        return False  # 시간 초과

modbus_service = ModbusService()
