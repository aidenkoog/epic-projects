from fastapi import APIRouter, WebSocket
from services.websocket_manager import websocket_manager

router = APIRouter()

@router.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket_manager.connect(websocket)
    try:
        while True:
            await asyncio.sleep(10)
    except Exception:
        websocket_manager.disconnect(websocket)
