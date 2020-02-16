import sys
from read_file import DataReader
from concurrent import futures
import pandas as pd
from dateutil import parser
from data_util import DateTimeHandler
import json

dataset = DateTimeHandler()



import threading, time, signal

from datetime import timedelta

WAIT_TIME_SECONDS = 3600

class ProgramKilled(Exception):
    pass

def foo():
    print(dataset)
    
def signal_handler(signum, frame):
    raise ProgramKilled


def chunk_parse(df):
	for _, row in df.iterrows():
		data=  row.to_json(orient='columns')
		d = json.loads(data)
		pick = parser.parse(d['tpep_pickup_datetime'])
		drop = parser.parse(d['tpep_dropoff_datetime'])
		
		dataset.append((pick.strftime('%Y-%m-%d') , pick.hour))
		dataset.append((drop.strftime('%Y-%m-%d') , drop.hour ))



    
class Job(threading.Thread):
	def __init__(self, interval, execute, *args, **kwargs):
		threading.Thread.__init__(self)
		self.daemon = False
		self.stopped = threading.Event()
		self.interval = interval
		self.execute = execute
		self.args = args
		self.kwargs = kwargs
		
	def stop(self):
		self.stopped.set()
		self.join()
		
	def run(self):
			while not self.stopped.wait(self.interval.total_seconds()):
				self.execute(*self.args, **self.kwargs)
			
if __name__ == "__main__":
	signal.signal(signal.SIGTERM, signal_handler)
	signal.signal(signal.SIGINT, signal_handler)
	job = Job(interval=timedelta(seconds=WAIT_TIME_SECONDS), execute=foo)
	job.start()


	drop = ['passenger_count','trip_distance','RatecodeID','store_and_fwd_flag','improvement_surcharge','total_amount',
			'payment_type','fare_amount','extra','mta_tax','tip_amount','tolls_amount']


	data_reader = DataReader(chunk_size=10**5).read_csv("mad.csv",drop_cols= drop)
	data = None
	# print(data_reader)
	# for d in data_reader:
	# 	# print()
	# 	if data is None:
	# 		data = d.window.getWindow('tpep_pickup_datetime')
	# 		print(type(data))
	# 		break
	# 	else:
	# 		pass
	# 		# data = pd.merge(data,d.window.getWindow('tpep_pickup_datetime'),how='outer').set_index(['tpep_pickup_datetime', 'tpep_pickup_datetime' ,'count']).sum(axis=1)
	# print(data)
	with futures.ThreadPoolExecutor(max_workers=4) as executor:
		futures = executor.map(chunk_parse, data_reader)
		# executor.c
		for f in futures:
			print(f)
			# sys.exit(0)

	# import dask.dataframe as dd
	# df = dd.read_csv("data.csv",blocksize=10**5)
	# print(df.size.compute() )
	# dataset = DateTimeHandler()
	# for d in data_reader:
	# 	d = json.loads(d)
	# 	pick = parser.parse(d['tpep_pickup_datetime'])
	# 	drop = parser.parse(d['tpep_dropoff_datetime'])
		
	# 	dataset.append((pick.strftime('%Y-%m-%d') , pick.hour))
	# 	# break

	print(dataset)


    
	while True:
		try:
			time.sleep(1)
		except ProgramKilled:
			print("Program killed: running cleanup code")
			job.stop()
			sys.exit(-1)
			break


