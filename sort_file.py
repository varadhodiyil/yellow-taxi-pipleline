import dask.dataframe as dd
from dateutil import parser
from datetime import datetime
import os
from geo_code import geo_code


def sort_csv(file_name, filter_s, filter_e, key='tpep_pickup_datetime', combine=dict(), fill_loc=False):
	start = datetime.now()
	df = dd.read_csv(file_name, dtype='str')

	if len(combine) > 0:
		to_col = combine['to_column']
		for i, col in enumerate(combine['columns']):
			if i == 0:
				df[to_col] = df[col]
			else:
				df[to_col] += ' ' + df[col]
			df = df.drop(col, axis=1)
			# df = df.compute()
	if fill_loc:
		df['BOROUGH'] = df.apply(lambda x: geo_code(
			x['BOROUGH'], x['LATITUDE'], x['LONGITUDE']), axis=1, meta=(None, 'object'))
	df[key] = dd.to_datetime(df[key])
	df = df.compute()
	filtered = df.loc[(df[key] >= filter_s) & (df[key] <= filter_e)]
	# filtered = filtered.loc[filtered[key] <= filter_e]
	filtered = filtered.compute()
	filtered = filtered.sort_values(by=[key])

	_, _file = os.path.split(file_name)
	sorted_file_name = 'sorted_{0}'.format(_file)
	filtered.to_csv(sorted_file_name, index=False)

	end_ = datetime.now()
	file_name = sorted_file_name
	print("Total Seconds for file %s sort %d " %
	      (file_name, (end_ - start).seconds))
	return file_name


# sort_csv('read/data.csv' , parser.parse('2018-01-01 00:00:00') , parser.parse('2018-02-01 ') )
# sort_csv('read/col.csv', parser.parse('2018-01-01 00:00:00'), parser.parse('2018-02-01 '), key='crash_date',
#          combine={'to_column': 'crash_date', 'columns': ['CRASH DATE', 'CRASH TIME']}, fill_loc=True)
