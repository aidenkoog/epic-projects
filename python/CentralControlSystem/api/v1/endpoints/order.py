from fastapi import APIRouter, Depends
from pydantic import BaseModel
from sqlalchemy.orm import Session
from database.crud import create_order
from database.database import get_db

router = APIRouter()

class OrderRequest(BaseModel):
    drink_type: str
    quantity: int

@router.post("/request")
async def process_order(order: OrderRequest, db: Session = Depends(get_db)):
    order = create_order(db, order.drink_type, order.quantity)
    return {"status": "success", "order_id": order.id}
