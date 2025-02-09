import serial
from config import SERIAL_PORT, BAUD_RATE

class SerialService:
    def __init__(self):
        self.ser = serial.Serial(SERIAL_PORT, BAUD_RATE)

    def send_data(self, data: str):
        self.ser.write(data.encode())

    def read_data(self):
        return self.ser.readline().decode().strip()

serial_service = SerialService()
