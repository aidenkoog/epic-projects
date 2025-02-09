import os

DATABASE_URL = os.getenv("DATABASE_URL", "sqlite:///./aidenkoog.db")
MODBUS_HOST = os.getenv("MODBUS_HOST", "192.168.0.100")
MODBUS_PORT = int(os.getenv("MODBUS_PORT", 502))
SOCKET_HOST = os.getenv("SOCKET_HOST", "192.168.0.200")
SOCKET_PORT = int(os.getenv("SOCKET_PORT", 5000))
SERIAL_PORT = os.getenv("SERIAL_PORT", "/dev/ttyUSB0")
BAUD_RATE = int(os.getenv("BAUD_RATE", 9600))
