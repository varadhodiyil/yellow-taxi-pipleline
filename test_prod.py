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


_start = datetime.now()
def chunk_parse(df, dest):
	# print( df)
	# df , dest = param
	print(dest)
	for _, row in df.iterrows():
		# print( dest)
		data = row.to_json(orient='columns')
		p.publish(data, destination=dest)


p = DataPublisher()


def read_files(file, drop, _dest):
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

try:
	t1 = threading.Thread(target=read_files, args=(
		"m.csv", drop, "/topic/dataset"))

	drop = ['ON STREET NAME', 'CROSS STREET NAME', 'OFF STREET NAME', 'NUMBER OF PERSONS INJURED', 'NUMBER OF PERSONS KILLED', 'NUMBER OF PEDESTRIANS INJURED', 
	'NUMBER OF PEDESTRIANS KILLED', 'NUMBER OF CYCLIST INJURED', 'NUMBER OF CYCLIST KILLED', 'NUMBER OF MOTORIST INJURED', 'NUMBER OF MOTORIST KILLED',
         'CONTRIBUTING FACTOR VEHICLE 1', 'CONTRIBUTING FACTOR VEHICLE 2', 'CONTRIBUTING FACTOR VEHICLE 3', 'CONTRIBUTING FACTOR VEHICLE 4', 
		 'CONTRIBUTING FACTOR VEHICLE 5', 'COLLISION_ID', 'VEHICLE TYPE CODE 1', 'VEHICLE TYPE CODE 2', 'VEHICLE TYPE CODE 3', 
		 'VEHICLE TYPE CODE 4', 'VEHICLE TYPE CODE 5']
	t2 = threading.Thread(target=read_files, args=(
		"a.csv", drop, "/topic/crash"))

	t1.start()
	t2.start()
# data_reader = DataReader(chunk_size=10**5).read_csv("m.csv",drop_cols= drop)
# data = None

# try:
# 	with futures.ThreadPoolExecutor(max_workers=4) as executor:
# 			futures = executor.map(chunk_parse, data_reader)
# 			# executor.c
# 			for f in futures:
# 				print(f)
except KeyboardInterrupt:
	sys.exit(-1)


_end = datetime.now()

print("Total Seconds %d " % (_end - _start).seconds )