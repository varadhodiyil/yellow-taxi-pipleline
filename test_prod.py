import sys
from read_file import DataReader
from concurrent import futures
import pandas as pd
from dateutil import parser
import threading
import json
from producer import DataPublisher
from datetime import datetime
import time
from sort_file import sort_csv


_start = datetime.now()

interested_start = parser.parse('2018-01-01')
interested_end = parser.parse('2018-02-01')


def chunk_parse(df, dest):
	# print( df)
	# df , dest = param
	print(dest)
	for _, row in df.iterrows():
		_key = None
		if 'tpep_pickup_datetime' in row:
			_key = 'tpep_pickup_datetime'
		if 'CRASH DATE' in row:
			_key = 'CRASH DATE'
		if parser.parse(row[_key]) >= interested_start and parser.parse(row[_key]) <= interested_end:
			data = row.to_json(orient='columns')
			p.publish(data, destination=dest)


p = DataPublisher()


def read_files(file, drop, _dest, sort=False, key='tpep_pickup_datetime'):
	if sort:
		file = sort_csv(file, interested_start, interested_end, key=key)
	data_reader = DataReader(chunk_size=10**3).read_csv(file, drop_cols=drop)
	for d in data_reader:
		chunk_parse(d, _dest)
		# params = [d,_dest]
	# with futures.ThreadPoolExecutor(max_workers=4) as executor:
	# 		# _futures = executor.map(chunk_parse, *zip(*params) )
	# 		_futures = executor.submit(chunk_parse, pd.DataFrame(data_reader) , _dest  )
	# 		# executor.c
	# 		print(_futures.result())


drop = ['passenger_count', 'trip_distance', 'RatecodeID', 'store_and_fwd_flag', 'improvement_surcharge', 'total_amount',
        'payment_type', 'fare_amount', 'extra', 'mta_tax', 'tip_amount', 'tolls_amount']


_dataset = "/topic/dataset"
_crash = "/topic/crash"
try:
	t1 = threading.Thread(target=read_files, args=(
		"sorted_data.csv", drop, _dataset, False))

	drop = ['ON STREET NAME', 'CROSS STREET NAME', 'OFF STREET NAME', 'NUMBER OF PERSONS INJURED', 'NUMBER OF PERSONS KILLED', 'NUMBER OF PEDESTRIANS INJURED',
         'NUMBER OF PEDESTRIANS KILLED', 'NUMBER OF CYCLIST INJURED', 'NUMBER OF CYCLIST KILLED', 'NUMBER OF MOTORIST INJURED', 'NUMBER OF MOTORIST KILLED',
         'CONTRIBUTING FACTOR VEHICLE 1', 'CONTRIBUTING FACTOR VEHICLE 2', 'CONTRIBUTING FACTOR VEHICLE 3', 'CONTRIBUTING FACTOR VEHICLE 4',
         'CONTRIBUTING FACTOR VEHICLE 5', 'COLLISION_ID', 'VEHICLE TYPE CODE 1', 'VEHICLE TYPE CODE 2', 'VEHICLE TYPE CODE 3',
         'VEHICLE TYPE CODE 4', 'VEHICLE TYPE CODE 5']
	t2 = threading.Thread(target=read_files, args=(
		"sorted_col.csv", drop, _crash, False, 'CRASH DATE'))

	t1.start()
	t2.start()
	t1.join()
	t2.join()
	# while not t1.is_alive() or not t2.is_alive():
	# 	pass
	d = json.dumps({"exit": True})
	p.publish(d, _dataset)
	p.publish(d, _crash)
# data_reader = DataReader(chunk_size=10**5).read_csv("m.csv",drop_cols= drop)
# data = None

# try:
# 	with futures.ThreadPoolExecutor(max_workers=4) as executor:
# 			futures = executor.map(chunk_parse, data_reader)
# 			# executor.c
# 			for f in futures:
# 				print(f)
except KeyboardInterrupt:
	d = {"exit": True}
	# p.publish(d, "")
	sys.exit(-1)


_end = datetime.now()

print("Total Seconds %d " % (_end - _start).seconds)
