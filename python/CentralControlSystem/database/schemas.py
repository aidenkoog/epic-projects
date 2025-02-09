from pydantic import BaseModel

class OrderRequest(BaseModel):
    drink_type: str
    quantity: int

class PaymentRequest(BaseModel):
    order_id: str
    amount: float
    status: str
