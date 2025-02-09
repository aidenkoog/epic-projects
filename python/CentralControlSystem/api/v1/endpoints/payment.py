from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from database.database import get_db
from database.crud import create_payment
from pydantic import BaseModel

router = APIRouter()

class PaymentRequest(BaseModel):
    order_id: str
    amount: float
    status: str

@router.post("/process")
async def process_payment(payment: PaymentRequest, db: Session = Depends(get_db)):
    saved_payment = create_payment(db, payment.order_id, payment.amount, payment.status)
    return {"status": "success", "payment_id": saved_payment.id}
