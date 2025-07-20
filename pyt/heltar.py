ho = int(input())
mi = int(input())
pe = int(input())

cur = ho * 60 + mi
deli = cur + pe
day = 24 * 60
qwe = deli % day
dh = qwe // 60
dm = qwe % 60

print(f"{dh:02}:{dm:02}")
