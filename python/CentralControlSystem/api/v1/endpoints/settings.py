from fastapi import APIRouter
from services.state_manager import state_manager

router = APIRouter()

@router.get("/status")
async def get_system_status():
    return state_manager.get_current_state()
