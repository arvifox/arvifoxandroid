#include <iostream>
#include <string>
#include <limits>
#include <cmath>
#include <vector>
#include <iterator>
#include <algorithm>
#include <cctype>
#include <utility>
#include <tuple>
#include <cstdint>
#include <array>
#include <list>
#include <forward_list>
#include <map>
#include <unordered_map>
#include <set>
#include <unordered_set>
#include <stack>
#include <queue>
#include <stdexcept>

int main() {
    Time t;
    // Считываем время в формате hh:mm:ss,
    // где на самом деле вместо двоеточия может быть любой разделитель
    std::cin >> t;

    t += 40;
    std::cout << t << "\n";  // печатаем время в формате hh:mm:ss
}

class Time
{
private:
    int hours;
    int minutes;
    int seconds;

public:
    Time(int h, int m, int s); // объявляем конструктор
    Time() = default;
    Time(int s): Time(0, 0, s) {
    }

    // Объявляем три функции для чтения полей:
    int GetHours() const;
    int GetMinutes() const;
    int GetSeconds() const;

    void AddSeconds(int s) {
        seconds += s;
    }

    Time& operator += (int s) {
       seconds += s;
       return *this;
   }

   Time operator + (int s) const {
       return Time(hours, minutes, seconds + s);
   }

   int TotalSeconds() const {
       return hours * 60 * 60 + minutes * 60 + seconds;
    }
};

int operator - (const Time& t1, const Time& t2) {
    return t1.TotalSeconds() - t2.TotalSeconds();
}

Time::Time(int h, int m, int s)
{
    if (s < 0 || s > 59)
    {
        // обрабатываем ошибочные секунды
    }
    if (m < 0 || m > 59)
    {
        // обрабатываем ошибочные минуты
    }
    if (h < 0 || h > 23)
    {
        // обрабатываем ошибочные часы
    }
    hours = h;
    minutes = m;
    seconds = s;
}

int Time::GetHours() const
{
    return hours;
}

int Time::GetMinutes() const
{
    return minutes;
}

int Time::GetSeconds() const
{
    return seconds;
}

// int Time::GetHours() const {
    // return this->hours;  // то же самое, что (*this).hours
// }

Time::Time(int h, int m, int s) {
    if (s < 0 || s > 59 || m < 0 || m > 59 || h < 0 || h > 23) {
        throw std::out_of_range("Wrong time!");
    }
    // ...
}

Time::Time(int h, int m, int s) {
    m += s / 60;
    s %= 60;
    // Если s было отрицательным, то остаток тоже будет отрицательным
    if (s < 0) {
        // Уменьшим в этом случае минуты и сделаем секунды положительными
        m -= 1;
        s += 60;
    }

    h += m / 60;
    m %= 60;
    if (m < 0) {
        h -= 1;
        m += 60;
    }

    h %= 24;
    if (h < 0) {
        h += 24;
    }

    hours = h;
    minutes = m;
    seconds = s;
}

std::ostream& operator << (std::ostream& out, const Time& t) {
    out << t.GetHours() << ":" << t.GetMinutes() << ":" << t.GetSeconds();
    return out;
}

std::istream& operator >> (std::istream& in, Time& t) {
    int h, m, s;
    char dummy;
    // Считываем число и любой непробельный символ за ним
    in >> h >> dummy;
    in >> m >> dummy;
    in >> s >> dummy;

    // У нас нет другого способа изменить время через публичный интерфейс
    // кроме присваивания нового значения
    t = Time(h, m, s);
    return in;
}
