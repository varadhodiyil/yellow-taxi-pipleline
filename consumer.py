import time
import sys
import json
import stomp

from datetime import datetime
start = datetime.now()

class MyListener(stomp.ConnectionListener):
	flag = 0
	def on_error(self, headers, message):
		print('received an error "%s"' % message)
		
	def on_message(self, headers, message):
		# data = json.loads(message)
		print("recieved {}".format(self.flag))
		self.flag = self.flag + 1 

		# print(message)
		# with open ("read/{0}.json".format(self.flag) , "w") as w:
		# 	json.dump(data,w , indent=4)
		# sys.exit(0)

conn = stomp.Connection([('192.168.137.1',61613)])
conn.set_listener('', MyListener())

conn.connect('admin', 'admin', wait=True)
conn.subscribe(destination='/topic/preprocess/cleanup', id=1, ack='auto')
# conn.send(body=' '.join(sys.argv[1:]), destination='/queue/test')
while True:
	time.sleep(2)
conn.disconnect()


print("done")
end = datetime.now()
print("Total Seconds" , (end-start).total_seconds())