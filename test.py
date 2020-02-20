import json
import signal
import sys
import time
from datetime import datetime, timedelta

from dateutil import parser

from consumer import DataReceiver
from data_util import DateTimeHandler
from job_scheduler import Job
from window import Window

dataset = DateTimeHandler()
current_hour = 0
current_day = 0
start_date = parser.parse('2018-01-01')
run_start = datetime.now()

_max = start_date
_min = start_date
w = Window()
r = DataReceiver(host='localhost')


def print_data():
	global current_hour
	global current_day
	print("*"*30)
	report_date = start_date + timedelta(days=current_day, hours=current_hour)
	print("Current Window {0} day , {1} hour".format(
		report_date, report_date.hour))
	r_date = report_date.strftime('%Y-%m-%d')
	if current_hour == 23:
		print("Daily Report")
		if r_date in dataset:
			hours = dataset[r_date]
			_sum = sum(hours.values())
			print("Daily Report")
			print("Peak At hour {0}".format(
				list(hours.values()).index(max(hours.values())) + 1))
			print(" Min {0} , Max {1} ".format(_min, _max))
		current_hour = -1
		current_day = current_day + 1

	current_hour = current_hour + 1

	if r_date in dataset:
		try:
			print(dataset[r_date][report_date.hour])
		except KeyError:
			pass


def data_parser(data):
	global _max
	global _min
	d = json.loads(data)
	if 'exit' in d:
		print('Exit')
		run_end = datetime.now()
		w.get_count()
		r.close()
		print("Total Seconds  %d " % (run_end - run_start).seconds)
		sys.exit(-10)
	pick = parser.parse(d['tpep_pickup_datetime'])
	# report_date = start_date + timedelta(days=current_day , hours= current_hour)
	# drop = parser.parse(d['tpep_dropoff_datetime'])
	w.count(pick, d['PULocationID'])
	# w.count(drop , d['DOLocationID'])
	# if report_date < pick and pick > start_date:
	# 	drop = parser.parse(d['tpep_dropoff_datetime'])
	# 	if max([pick, drop]) > _max :
	# 		_max = max([pick,drop])
	# 	if min([pick,drop]) < _min:
	# 		_min = min([pick, drop])
	# 	dataset.append((pick.strftime('%Y-%m-%d') , pick.hour))
	# 	dataset.append((drop.strftime('%Y-%m-%d') , drop.hour ))


def exit_handler(signum, frame):
	print("Exiting")
	# job.stop()
	# r.close()


class ProgramKilled(Exception):
    pass


def signal_handler(signum, frame):
    raise ProgramKilled


r.subscribe('/topic/dataset', data_parser)
# job = Job(60,print_data)
# job.start()


# signal.signal(signal.SIGTERM, exit_handler)
# signal.signal(signal.SIGINT, exit_handler)


while True:
	time.sleep(0.1)
