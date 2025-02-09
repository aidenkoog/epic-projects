from fastapi import WebSocket, WebSocketDisconnect
from typing import List
import asyncio
from services.state_manager import state_manager

class WebSocketManager:
    def __init__(self):
        self.active_connections: List[WebSocket] = []

    async def connect(self, websocket: WebSocket):
        await websocket.accept()
        self.active_connections.append(websocket)

    def disconnect(self, websocket: WebSocket):
        self.active_connections.remove(websocket)

    async def broadcast(self, message: str):
        for connection in self.active_connections:
            await connection.send_text(message)

    async def broadcast(self):
        while True:
            state = state_manager.get_current_state()
            for connection in self.active_connections:
                try:
                    await connection.send_json(state)
                except WebSocketDisconnect:
                    self.disconnect(connection)
            await asyncio.sleep(1)

websocket_manager = WebSocketManager()
