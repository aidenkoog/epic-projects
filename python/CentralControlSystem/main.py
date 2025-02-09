from fastapi import FastAPI
from api.v1.endpoints import order, system, payment, device, settings
from services.websocket_manager import websocket_app
from database.database import init_db

app = FastAPI(title="AidenKooG Server")

init_db()

app.include_router(order.router, prefix="/order", tags=["Order"])
app.include_router(system.router, prefix="/system", tags=["System"])
app.include_router(payment.router, prefix="/payment", tags=["Payment"])
app.include_router(device.router, prefix="/device", tags=["Device"])
app.include_router(settings.router, prefix="/settings", tags=["Settings"])

app.mount("/ws", websocket_app)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="192.168.0.1", port=8000)
