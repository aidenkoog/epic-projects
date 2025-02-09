import asyncio
import socket
from config import SOCKET_HOST, SOCKET_PORT

async def send_command(order_id, drink_type, quantity):
    try:
        reader, writer = await asyncio.open_connection(SOCKET_HOST, SOCKET_PORT)
        command = f"ORDER {order_id} {drink_type} {quantity}\n"
        writer.write(command.encode())
        await writer.drain()

        response = await reader.read(100)
        writer.close()
        await writer.wait_closed()
        return response.decode()
    except Exception as e:
        return str(e)

async def connect_socket():
    try:
        reader, writer = await asyncio.open_connection(SOCKET_HOST, SOCKET_PORT)
        writer.write(b"CONNECT\n")
        await writer.drain()

        response = await reader.read(100)
        writer.close()
        await writer.wait_closed()
        return response.decode()
    except Exception as e:
        return str(e)