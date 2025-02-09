from fastapi import APIRouter

router = APIRouter()

@router.post("/device_test1")
async def device_test1(id: int):
    # TODO:
    return {"status": "success", "message": f"Device Test1 {id} triggered"}

@router.post("/device_test2")
async def device_test2(id: int):
    # TODO:
    return {"status": "success", "message": f"Device Test2 {id} activated"}

@router.post("/reset_firmware")
async def reset_firmware():
    # TODO:
    return {"status": "success", "message": "Reset Triggered"}
