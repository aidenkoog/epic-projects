import pytest
from services.state_manager import state_manager

def test_state_manager():
    state_manager.update_state("modbus", True)
    assert state_manager.get_current_state()["modbus"] == True
