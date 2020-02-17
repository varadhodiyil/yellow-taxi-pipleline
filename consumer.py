import time
import sys
import json
import stomp

from datetime import datetime
start = datetime.now()


class MyListener(stomp.ConnectionListener):
	flag = 0

	def __init__(self, callback=None):
		self.callback = callback

	def set_callback(self, data):
		self.callback = data

	def on_error(self, headers, message):
		print('received an error "%s"' % message)

	def on_message(self, headers, message):
		# data = json.loads(message)
		if self.callback is not None:
			self.callback(message)

		# print(message)
		# with open ("read/{0}.json".format(self.flag) , "w") as w:
		# 	json.dump(data,w , indent=4)
		# sys.exit(0)


class DataReceiver():
	def __init__(self, host="localhost", port=61613, callback=None):
		self.callback = callback
		self.conn = stomp.Connection([(host, port)])
		self.listener = MyListener(callback=callback)
		self.conn.set_listener('', self.listener)
		self.conn.connect(wait=True)

	def subscribe(self, destination, callback):
		self.listener.set_callback(callback)
		self.conn.subscribe(destination=destination, id=1, ack='auto')

	def close(self):
		self.conn.disconnect()






if __name__ == "__main__":
	d = DataReceiver()
	d.subscribe("/topic/crash",print)
	while True:
		time.sleep(2)