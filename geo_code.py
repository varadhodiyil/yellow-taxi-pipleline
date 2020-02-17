import requests
import pandas as pd
import os
import json
Loc_data = "GeoCode.csv"
data = pd.DataFrame(columns=['latlng', 'Borough'])
if os.path.exists(Loc_data):
	data = pd.read_csv(Loc_data)
data = data.append({"latlng": "1,2", "Borough": "Mad"}, ignore_index=True)


def geo_code(lat, lng):
	global data
	loc = "{0},{1}".format(lat, lng)
	_data = data['latlng'] == loc
	__data = data[_data]
	if data[_data]['latlng'].count() == 0:
		url = "https://maps.google.com/maps/api/geocode/json?latlng={0}&key=AIzaSyB3AMNbQkN3IgEQvCO9q6jmaWqHDRVeQA8".format(
			loc)
		resp = requests.get(url).json()
		# with open("t.json","r") as j:
		# 	resp = json.load(j)
		# print(resp)
		if len(resp['results']) > 0:
			for comp in resp['results']:
				for a in comp['address_components']:
					print(a)
					if 'sublocality_level_1' in a['types']:
						name = a['short_name']
						data = data.append({"latlng": loc, "Borough": name}, ignore_index=True)
						data.to_csv(Loc_data)
						return name
	else:
		return __data.iloc[0]['Borough']


