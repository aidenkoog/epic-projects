from pymodbus.client import ModbusTcpClient
from config import MODBUS_HOST, MODBUS_PORT

class ModbusService:
    def __init__(self):
        self.client = ModbusTcpClient(MODBUS_HOST, MODBUS_PORT)

    def send_command(self, register, value):
        self.client.write_register(register, value)

    def read_status(self, register):
        return self.client.read_holding_registers(register, 1).registers[0]

modbus_service = ModbusService()
