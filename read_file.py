import pandas as pd
# import analytics
from datetime import datetime


class DataReader(object):
	def __init__(self, chunk_size=10**4):
		super().__init__()
		self.chunk_size = chunk_size

	def read_csv(self, file_name="data.csv", drop_cols=None):
		data = pd.read_csv(file_name, chunksize=self.chunk_size, engine="c",
                     low_memory=False, date_parser=lambda x: datetime.strptime(x, '%Y-%m-%d %H:%M'))
		for chunk in data:
			if drop_cols is not None:
				chunk = chunk.drop(drop_cols, axis=1)
			# print(chunk)
			yield chunk
			# for _, row in chunk.iterrows():
			# 	yield row.to_json(orient='columns')
		# return data
