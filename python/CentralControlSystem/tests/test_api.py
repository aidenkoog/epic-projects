from fastapi.testclient import TestClient
from main import app

client = TestClient(app)

def test_order_request():
    response = client.post("/order/request", json={"drink_type": "type1", "quantity": 2})
    assert response.status_code == 200
    assert response.json()["status"] == "success"

def test_system_on():
    response = client.post("/system/on")
    assert response.status_code == 200
    assert response.json()["status"] == "success"

def test_device1():
    response = client.post("/device/device_test1", json={"id": 1})
    assert response.status_code == 200
    assert response.json()["status"] == "success"
