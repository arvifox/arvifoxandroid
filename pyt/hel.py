phrase = input("Введите строку: ")
print(phrase)

name = "Пользователь"
print("Добрый день,", name, ".")
print("Добрый день, ", name, ".", sep="")

nameq = "Пользователь"
print(f"Добрый день, {nameq}.")

print(f"{123:0>9}") # 000000123 (выравнивание по правому краю)
print(f"{123:0<9}") # 123000000 (выравнивание по левому краю)
print(f"{123:0^9}") # 000123000 (выравнивание по центру)

print("Привет, {}!".format(name))
print("Привет, %s!" % name)

print("-" * 10)

a = 2 # целое число
b = 2.0 # вещественное число

n_1 = "1"
n_2 = "2"
print(n_1 + n_2)
n_1 = int(n_1)
n_2 = int(n_2)
print(n_1 + n_2)

int_number = int(input())
float_number = float(input())

n = 25
x = 0.5

print(n + x)
print(n - x)
print(n * x)
print(n / x)
print(n ** x)

print(f"{2 ** 0.5:.2f}") # 2 в степени 0.5 вывести с точностью до двух знаков

last_digit = 1234 % 10

binary_value = "1001"
print(int(binary_value, 2))

inhex = hex(927495) # bin, oct

print(round(3.14159, 2))  # выведет 3.14
print(f'{3.14159:.2f}')  # выведет 3.14

value = 42
print(f"!{value:10}!")  # Ширина 10 символов, выравнивание по правому краю
# !        42!

value = 42
print(f"!{value:<10}!")  # Ширина 10 символов, выравнивание по левому краю
# !42        !

value = 42
print(f"!{value:^10}!")  # Ширина 10 символов, выравнивание по центру
# !    42    !

value = 42
print(f"{value:.^10}")  # Ширина 10 символов, заполняем точками
# ....42....

print(ord("t"), ord("w"))
print(chr(116), chr(119))

text_1 = "Привет"
text_2 = "привет"
print(text_1.lower() == text_2.lower())  # True

text = input()
if "добр" in text:
    print("Встретилось 'доброе' слово.")
else:
    print("Добрых слов не найдено.")

color = input()
match color:
    case 'красный' | 'жёлтый':
        print('Стоп.')
    case 'зелёный':
        print('Можно ехать.')
    case _:
        print('Некорректное значение.')

m = 12
n = 19
k = 25
# максимальное число
print(max(m, n, k))
line_1 = "m"
line_2 = "n"
line_3 = "k"
# минимальная лексикографически строка
print(min(line_1, line_2, line_3))
# количество цифр в числе 2 в степени 2022
print(len(str(2 ** 2022)))

