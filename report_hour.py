from consumer import DataReceiver
import json
import time

def print_report(data):
	d = json.loads(data)
	print(json.dumps(d,indent=4))

r = DataReceiver()



r.subscribe("/topic/report/hour",print_report)


while True:
	time.sleep(0.1)