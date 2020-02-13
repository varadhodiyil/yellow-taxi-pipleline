import time
import sys
import json
import stomp

class MyListener(stomp.ConnectionListener):
	flag = 0
	def on_error(self, headers, message):
		print('received an error "%s"' % message)
		
	def on_message(self, headers, message):
		# data = json.loads(message)
		print("recieved")
		print(message)
		# with open ("read/{0}.json".format(self.flag) , "w") as w:
		# 	json.dump(data,w , indent=4)
		sys.exit(0)

conn = stomp.Connection([('localhost',61613)])
conn.set_listener('', MyListener())

conn.connect('admin', 'admin', wait=True)
conn.subscribe(destination='/topic/dataset', id=1, ack='auto')
# conn.send(body=' '.join(sys.argv[1:]), destination='/queue/test')
while True:
	time.sleep(2)
conn.disconnect()