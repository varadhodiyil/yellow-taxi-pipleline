from collections import defaultdict 
import json
class DateTimeHandler(dict):
	data = defaultdict(dict)	
	def __init__(self):
		pass
	
	def append(self,_data):
		date , hour  = _data
		if hour not in self.data[date]:
			self.data[date][hour] = 0
		self.data[date][hour] += 1


	def __str__(self):
		return json.dumps(self.data)



# d = DateTimeHandler()
# d.append((1,8))
# d.append((1,9))

# print(d)




