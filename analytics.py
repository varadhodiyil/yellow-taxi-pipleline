import pandas as pd

@pd.api.extensions.register_dataframe_accessor("window")
class Window():

	def __init__(self , pandas_df , window_size = 3600 , col='tpep_pickup_datetime'):
		self.window_size = window_size
		self._obj = pandas_df
		self._obj[col] = pd.to_datetime(self._obj[col], format='%Y-%m-%d %H:%M:%S')

	def getWindow(self, col):
		# _df = self._obj.groupby([self._obj[col].dt.date, self._obj[col].dt.hour],as_index=False).count()
		_df = self._obj.groupby(by=[self._obj[col].dt.date, self._obj[col].dt.hour] , as_index=True)[col].count().to_frame().apply(list)
		return _df

	

# class Window(list):
# 	def __init__(self, window = 3600, *args, **kwargs):
# 	 super().__init__(*args, **kwargs)
# 	 self.window = window

# 	def append(self, object):
# 	 return super().append(object)