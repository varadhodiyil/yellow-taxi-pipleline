import random
from datetime import datetime
from datetime import timedelta, datetime
import signal
from dateutil import parser
import numpy as np
import operator
from collections import defaultdict


class Window(object):
	def __init__(self, start='2018-01-01', zone=''):
		self.hour = 0
		self.day = np.zeros(24, dtype=int)
		self.current_hour = 0
		self.current_day = 0
		self.start_date = parser.parse(start)
		# self.current_time = self.start_date + timedelta(days=self.current_day , hours= self.current_hour)
		self.flag = 0
		self.current_time = None
		self.zone = defaultdict(int)

	def count(self, data, zone):
		if self.current_time is None:
			self.current_time = data
		if data.day != self.current_time.day:
			print("Next Window" )
			print("For hour {0} total Trips {1} ".format(self.current_hour, self.hour))
			_sorted = sorted(self.zone.items() , key= operator.itemgetter(1),reverse=True)
			if len(_sorted) > 0:
				key , val = _sorted [0]
				print("Peak Zone {0} with frequency {1}" .format(key,val))
			# print("Day Window")
			# print("Total Trips {0}".format(sum(self.day)) )
			# print("Peak Hour" , list(self.day).index(max(self.day)))
			print("." * 100)
			
			print("*" * 5, self.current_time, data)
			print("Day Window", self.current_time)
			# print(self.day)
			print("Total Trips {0} ".format(sum(self.day)))
			print("Peak Hour", list(self.day).index(max(self.day)))

			# self.current_day = data.day
			# self.current_hour = data.hour
			# self.hour = 0
			# self.current_time = self.start_date + timedelta(days=self.current_day , hours= self.current_hour)
			print("-" * 100)
			print('Next Day')
			self.day[self.current_hour] = self.hour
			self.current_day = data.day
			self.hour = 0
			self.current_hour = data.hour
			self.current_time = data
			self.day = np.zeros(24, dtype=int)
			self.zone = defaultdict(int)
		if (data.hour - self.current_time.hour) >= 1:
			# self.hour = self.hour + 1
			
			print("For hour {0} total Trips {1} ".format(self.current_hour, self.hour))
			_sorted = sorted(self.zone.items() , key= operator.itemgetter(1),reverse=True)
			if len(_sorted) > 0:
				key , val = _sorted [0]
				print("Peak Zone {0} with frequency {1}" .format(key,val))
			self.flag = self.flag + 1
			self.day[self.current_hour] = self.hour
			self.current_hour = data.hour
			self.hour = 0
			self.zone = defaultdict(int)
			self.current_time = data
		# else:
		# 	if self.flag > 1:

		# 		self.day[self.current_hour] = self.hour
		# 		# print(" Diff For hour {0} total Trips {1}".format (self.current_hour , self.hour) )
		# 		# self.current_time = self.start_date + timedelta(days=self.current_day , hours= self.current_hour)
		# 		self.current_time = data
		# 		self.hour = 0
		# 		self.flag = 0
			# else:
			# 	self.hour = self.hour + 1
		if self.current_hour >= 24 or (data.day != self.current_time.day):

			print("*" * 5, self.current_time, data)
			print("Day Window", self.current_time)
			# print(self.day)
			print("Total Trips {0} ".format(sum(self.day)))
			print("Peak Hour", list(self.day).index(max(self.day)))

			# self.current_day = data.day
			# self.current_hour = data.hour
			# self.hour = 0
			# self.current_time = self.start_date + timedelta(days=self.current_day , hours= self.current_hour)
			print("-" * 100)
			# print("Next Day")
			self.day = np.zeros(24, dtype=int)

		self.hour = self.hour + 1
		self.zone[zone] += 1

	def get_count(self):
		if len(self.day) > 0:
			if(self.hour) > 0:
				self.day[self.current_hour] = self.hour
			print("For hour {0} total Trips {1}".format(self.current_hour, self.hour))
			_sorted = sorted(self.zone.items() , key= operator.itemgetter(1),reverse=True)
			if len(_sorted) > 0:
				key , val = _sorted [0]
				print("Peak Zone {0} with frequency {1}" .format(key,val))
			print("*" * 50)
			print("Day Window for ", self.current_time.strftime('%Y-%m-%d'))
			print("Total Trips {0}".format(sum(self.day)))
			print("Peak Hour", list(self.day).index(max(self.day)))
			# print(self.day)


# w = Window()
# w.count(parser.parse('2018-01-01 00:00:01'), 'M')
# w.count(parser.parse('2018-01-01 00:10:01'), 'M')
# w.count(parser.parse('2018-01-01 00:00:01'), 'H')
# w.count(parser.parse('2018-01-01 01:00:01'), 'M')
# w.count(parser.parse('2018-01-01 01:00:01'), 'M')
# # for _ in range(11):
# # 	w.count(parser.parse('2018-01-01 02:00:01'))
# # 	w.count(parser.parse('2018-01-01 02:00:01'))
# w.count(parser.parse('2018-01-23 02:00:01'), 'H')
# w.count(parser.parse('2018-01-23 04:00:01'), 'H')
# w.count(parser.parse('2018-01-23 04:00:01'), 'H')


# # for _ in range(10):
# # 	r = random.randint(0,23)
# # 	print("Rand" , r)
# # 	w.count(parser.parse('2018-01-01 02:00:01') + timedelta(hours=r))
# # 	w.count(parser.parse('2018-01-01 02:00:01') + timedelta(days=r))
# if w.hour > 0:
# 	w.get_count()
