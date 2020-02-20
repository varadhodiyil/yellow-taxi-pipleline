from consumer import DataReceiver
import json
import time
from dateutil.parser import parse

crash_data = {}


# def reverse(string): 
#     string = "".join(reversed(string)) 
#     return string 


def get_crash(data):
	d = json.loads(data)
	# print(json.dumps(d,indent=4))
	t = d['crashDateTime'].replace("/","-")
	t = t.split()
	_parsed = "{0} {1}".format( parse(t[0]).strftime('%Y-%m-%d') , t[1])
	# parsed = reverse("{0} {1}".format(parsed[0],parsed[1] ))
	crash_data[_parsed] = d


def get_hour(data):
	d = json.loads(data)
	# print(d)
	r = "{0} {1}".format(d['date'], d['hour'])
	# print(r in crash_data , crash_data.keys() , r)
	if r in crash_data:
		d['crash_report'] = dict(crash_data[r])
		del crash_data[r]
		print(json.dumps(d,indent=4))
	else:
		print(json.dumps(d,indent=4))


r = DataReceiver()


r.subscribe("/topic/report/crash",get_crash)
h = DataReceiver()
h.subscribe("/topic/report/hour",get_hour)

while True:
	time.sleep(0.1)

# t = "1/21/2018 0".replace("/","-")
# t = t.split()
# _parsed = "{0} {1}".format( parse(t[0]).strftime('%Y-%m-%d') , t[1])
# print(_parsed)