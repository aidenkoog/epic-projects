from sqlalchemy.orm import Session
from database.models import Payment, Order

def create_payment(db: Session, order_id: str, amount: float, status: str):
    payment = Payment(order_id=order_id, amount=amount, status=status)
    db.add(payment)
    db.commit()
    db.refresh(payment)
    return payment

def create_order(db: Session, drink_type: str, quantity: int):
    order = Order(drink_type=drink_type, quantity=quantity)
    db.add(order)
    db.commit()
    db.refresh(order)
    return order
