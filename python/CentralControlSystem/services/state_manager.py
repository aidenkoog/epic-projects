class StateManager:
    def __init__(self):
        self.state = {
            "modbus": False,
            "device": False,
            "serial": False,
            "websocket": False
        }

    def update_state(self, key, value):
        if key in self.state:
            self.state[key] = value

    def get_current_state(self):
        return self.state

state_manager = StateManager()
