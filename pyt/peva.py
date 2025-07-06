pen = "Петя"
vtn = "Вася"
trn = "Толя"
pes = int(input())
vts = int(input())
trs = int(input())

if vts > pes:
    vts, pes = pes, vts
    vtn, pen = pen, vtn
if trs > vts:
    trs, vts = vts, trs
    trn, vtn = vtn, trn
if vts > pes:
    vts, pes = pes, vts
    vtn, pen = pen, vtn

print(f"1. {pen}")
print(f"2. {vtn}")
print(f"3. {trn}")
