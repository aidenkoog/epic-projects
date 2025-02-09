from fastapi import Depends
from sqlalchemy.orm import Session
from database.database import SessionLocal

def get_db():
    db: Session = SessionLocal()
    try:
        yield db
    finally:
        db.close()
