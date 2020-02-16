import stomp
from read_file import DataReader
from datetime import datetime
import sys
conn = stomp.Connection([('192.168.1.14',61613)])

start = datetime.now()

# from concurrent import futures

def chunk_parse(df):
	conn.send(body=df,destination='/topic/dataset')
	# data = df.apply(lambda x: x.to_json(), axis=1)

conn.connect('admin', 'password', wait=True)
conn.connect(wait=True)
for _ in range(1):
	# conn.send(body=' '.join(sys.argv[1:]), destination='/queue/test')
	data_reader = DataReader(chunk_size=10**4).read_csv(file_name="mad.csv")
	# with futures.ThreadPoolExecutor(max_workers=4) as executor:
	# 	futures = executor.map(chunk_parse, data_reader)
	# flag = 0
	# print(data_reader.to_json(orient='records', lines=True))
	for chunk in data_reader:
		print(chunk)
		conn.send(body=chunk,destination='/topic/dataset')
print("done")
end = datetime.now()
print("Total Seconds" , (end-start).total_seconds())


conn.disconnect()