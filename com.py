from functools import lru_cache

@lru_cache(maxsize=128)
def fact(n):
	 
	if n <=0 :
		return 1
	if n ==1 :
		return n
	return n * fact(n-1)

def ncr(n,r):
    return int(fact(n) / (fact(r) * fact(n-r)))


# print(ncr(2,2))
from itertools import combinations

def f(m,w):

	M = m
	W = w
	val = 0

	flag = 0
	while m > 0 and w > 0 :
		# print(m,w , ncr(m,1) * ncr(w,2))
		_m = m 
		_w = 3 - _m 
		# print(_m,_w, m,w)
		val += ncr(M,_m) * ncr(W,_w)
		if flag %2 ==0 :
			m = m -1
		else:
			w = w -1 
	return val
print(f(1,3))