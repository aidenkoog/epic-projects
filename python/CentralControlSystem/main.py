import asyncio
from fastapi import FastAPI
from api.v1.endpoints import order, system, payment, device, settings, websocket
from services.websocket_manager import websocket_manager
from services.state_manager import state_manager

app = FastAPI(title="AidenKooG Server")

app.include_router(order.router, prefix="/order", tags=["Order"])
app.include_router(system.router, prefix="/system", tags=["System"])
app.include_router(payment.router, prefix="/payment", tags=["Payment"])
app.include_router(device.router, prefix="/device", tags=["Device"])
app.include_router(settings.router, prefix="/settings", tags=["Settings"])
app.include_router(websocket.router)

@app.on_event("startup")
async def startup_event():
    asyncio.create_task(state_manager.update_state())
    asyncio.create_task(websocket_manager.broadcast())

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
