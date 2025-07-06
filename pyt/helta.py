che = input()
pri = int(input())
we = int(input())
mon = int(input())
print(f"{'Чек':=^35}")
print(f"Товар:{che:>29}")
priwe = f"{we}кг * {pri}руб/кг"
print(f"Цена:{priwe:>30}")
itog = f"{we * pri}руб"
print(f"Итого:{itog:>29}")
vnes = f"{mon}руб"
print(f"Внесено:{vnes:>27}")
sda = f"{mon - we * pri}руб"
print(f"Сдача:{sda:>29}")
print("=" * 35)
