import requests
import pandas as pd
import os
import json
import numpy as np
Loc_data = "GeoCode.csv"
data = pd.DataFrame(columns=['latlng', 'Borough'])
if os.path.exists(Loc_data):
	data = pd.read_csv(Loc_data)
# data = data.append({"latlng": "1,2", "Borough": "Mad"}, ignore_index=True)


def geo_code(borough, lat, lng):
	if type(borough) == str and len(borough) > 1:
		return borough
	global data
	_loc = "{0},{1}".format(lat, lng)
	# _data =
	__data = data.loc[data['latlng'] == _loc]
	if __data['latlng'].count() == 0:
		# url = "https://maps.google.com/maps/api/geocode/json?latlng={0}&key=AIzaSyB3AMNbQkN3IgEQvCO9q6jmaWqHDRVeQA8".format(
		# 	_loc)
		# resp = requests.get(url).json()
		with open("t.json","r") as j:
			resp = json.load(j)
		# print(resp)
		if len(resp['results']) > 0:
			for comp in resp['results']:
				for a in comp['address_components']:
					if 'sublocality_level_1' in a['types']:
						name = a['short_name']
						data = data.append({"latlng": _loc, "Borough": name}, ignore_index=True)
						data.to_csv(Loc_data, index=False)
						return name
	else:
		return __data.iloc[0]['Borough']
