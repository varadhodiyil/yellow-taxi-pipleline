from datetime import datetime
from datetime import timedelta , datetime
import signal
from dateutil import parser



class Window(object):
	def __init__(self ,start = '2018-01-01'):
		self.hour = 0
		self.day = list()
		self.current_hour = 0
		self.current_day = 0
		self.start_date =  parser.parse(start)
		# self.current_time = self.start_date + timedelta(days=self.current_day , hours= self.current_hour)
		self.flag = 0
		self.current_time = None

	def count(self,data):
		if self.current_time is None:
			self.current_time = data
		print(data.hour , self.current_time.hour)
		if (data.hour - self.current_time.hour) >= 1:
			print(" For hour {0} total Trips {1}".format (self.current_hour , self.hour) )
		else:
			self.flag = self.flag + 1
			if self.flag > 10:
				self.current_hour = self.current_hour + 1
				self.day.append(self.hour)
				print(" For hour {0} total Trips {1}".format (self.current_hour , self.hour) )
				self.current_time = self.start_date + timedelta(days=self.current_day , hours= self.current_hour)
				self.hour = 0
			else:
				self.hour = self.hour + 1
		if self.current_hour >= 24:
			print("Day Window {0}".format(sum(self.day)) )
			self.current_day = self.current_day + 1
			self.hour = 0
			self.day = list()

		
	
	def get_count(self):
		print(" For hour {0} total Trips {1}".format (self.current_hour , self.hour) )


w = Window()
w.count(parser.parse('2018-01-01 00:00:01'))
w.count(parser.parse('2018-01-01 00:00:01'))
w.count(parser.parse('2018-01-01 01:00:01'))
for _ in range(11):
	w.count(parser.parse('2018-01-01 02:00:01'))
	w.count(parser.parse('2018-01-01 01:00:01'))
if w.hour > 0:
	w.get_count()