from collections import defaultdict 
import json
from dateutil import parser
from datetime import timedelta
class DateTimeHandler(defaultdict):
	data = defaultdict(dict)
	
	def __init__(self):
		pass
	
	def __keytransform__(self, key):
		return key

	def append(self,_data):
		date , hour  = _data
		if hour not in self.data[date]:
			self.data[date][hour] = 0
		self.data[date][hour] += 1


	def __str__(self):
		return json.dumps(self.data)

	def __getitem__(self,name):
		if name in self.data:
			return self.data[name]
	

	def __contains__(self, value):
		# print("eq")
		# return super().__contains__(value)
		if value in self.data:
			return True
		return False
	
if __name__ == "__main__":
	start_date =  parser.parse('2018-01-01')
	d = DateTimeHandler()
	d.append((start_date.strftime('%Y-%m-%d'),8))
	d.append(( (start_date + timedelta(days=1)).strftime('%Y-%m-%d'),9))
	c = parser.parse('2018-01-01')  + timedelta(days=2)
	print(d[c.strftime('%Y-%m-%d')])
	print(c.strftime('%Y-%m-%d') in d)
	print(d)




