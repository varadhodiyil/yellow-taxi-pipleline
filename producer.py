import stomp


class DataPublisher():
    def __init__(self, host="localhost", port=61613):
        self.conn = stomp.Connection([(host, port)])
        self.conn.connect(wait=True)

    def publish(self, data, destination="/topic/dataset"):
        self.conn.send(body=data, destination=destination)

    def close(self):
        self.conn.disconnect()
