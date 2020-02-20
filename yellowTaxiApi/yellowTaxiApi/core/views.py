
from rest_framework.generics import GenericAPIView
from rest_framework.response import Response
from rest_framework.parsers import JSONParser, MultiPartParser

from yellowTaxiApi.core.consumer import DataReceiver
import json
import time
from dateutil.parser import parse

crash_data = {}

current_hour = {}
current_day = {}
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
	global current_hour
	d = json.loads(data)
	# print(d)
	r = "{0} {1}".format(d['date'], d['hour'])
	# print(r in crash_data , crash_data.keys() , r)
	if r in crash_data:
		d['crash_report'] = dict(crash_data[r])
		del crash_data[r]
	current_hour = d


def get_day(data):
	data = json.loads(data)
	global current_day
	current_day = data


r = DataReceiver()


r.subscribe("/topic/report/crash",get_crash)
h = DataReceiver()
h.subscribe("/topic/report/hour",get_hour)

h.subscribe("/topic/report/daily",get_day)





class HourlyWindow(GenericAPIView):

    parser_class = ((JSONParser, MultiPartParser))

    def get(self, request, *args, **kwargs):
        _dict = dict()
        return Response(current_hour)



class DailyWindow(GenericAPIView):

    parser_class = ((JSONParser, MultiPartParser))

    def get(self, request, *args, **kwargs):
        _dict = dict()
        return Response(current_day)
