import pandas as pd


class DataReader(object):
	def __init__(self, chunk_size = 10**4):
		super().__init__()
		self.chunk_size = chunk_size

	def read_csv(self,file_name="data.csv"):
		data = pd.read_csv(file_name, chunksize=self.chunk_size ,engine = "c",
		    low_memory=False )
		for chunk in data:
			# print(chunk)
			yield chunk.to_json(orient='records')
		# return data