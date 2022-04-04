import json
from locust import HttpUser, between, task


class WebsiteUser(HttpUser):
    wait_time = between(1, 1.2)
    
    @task
    def index(self):
        payload = {
            "username": "test",
            "password": "test",
            "message": "Stress test message"
        }
        
        headers = {'content-type': 'application/json'}
        self.client.verify = False
        self.client.post("/server/verification", data=json.dumps(payload), headers=headers )
        
