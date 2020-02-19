import dask.dataframe as dd
from dateutil import parser
from datetime import datetime
import os
def sort_csv(file_name , filter_s, filter_e , key='tpep_pickup_datetime'):

	start = datetime.now()
	df = dd.read_csv(file_name, dtype='str')
	df[key] = dd.to_datetime(df[key])
	filtered = df[df[key] >= filter_s]   
	filtered = filtered[filtered[key] <= filter_e]
	filtered = filtered.compute()
	filtered = filtered.sort_values(by=[key])

	_ , _file = os.path.split(file_name)
	sorted_file_name = 'sorted_{0}'.format(_file)
	filtered.to_csv(sorted_file_name,index=False)

	end_ = datetime.now()
	file_name = sorted_file_name
	print("Total Seconds for file %s sort %d " % ( file_name, (end_ - start).seconds ))
	return file_name

# sort_csv('read/data.csv' , parser.parse('2018-01-01 00:00:00') , parser.parse('2018-02-01 ') )