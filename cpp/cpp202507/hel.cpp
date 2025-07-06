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

int main()
{
    std::string name;
    std::cout << "Hello world\n";
    // std::cin >> name;
    std::getline(std::cin, name);
    int absvalue = std::abs(234.0 - 4949);
    std::cout << "hey " << name << " dude";
}

void vectoris()
{
    std::vector<int> data = {1, 2, 3, 4, 5};
    for (int elem : data)
    {
        std::cout << elem << " ";
    }
    std::cout << "\n";
    std::cout << data.size() << "\n";

    std::vector<std::string> v1;             // пустой вектор строк
    std::vector<std::string> v2(5);          // вектор из пяти пустых строк
    std::vector<std::string> v3(5, "hello"); // вектор из пяти строк "hello"

    for (size_t i = 0; i != data.size(); ++i)
    {
        std::cout << data[i] << " ";
    }

    for (std::int64_t i = 0; i < std::ssize(data) - 1; ++i)
    {
        if (data[i] == data[i + 1])
        {
            std::cout << "Duplicate value: " << data[i] << "\n";
        }
    }

    std::sort(data.begin(), data.end());
    std::ranges::sort(data);

    std::vector<std::string> words;
    // Размер вектора остаётся нулевым, меняется только резерв:
    words.reserve(12);

    std::cout << data[42] << "\n";    // неопределённое поведение: может произойти всё что угодно
    std::cout << data.at(0) << "\n";  // напечатается 1
    std::cout << data.at(42) << "\n"; // произойдёт исключение std::out_of_range — его можно будет перехватить и обработать

    size_t m, n;
    std::cin >> m >> n; // число строк и столбцов
    // создаём матрицу matrix из m строк, каждая из которых — вектор из n нулей
    std::vector<std::vector<int>> matrix(m, std::vector<int>(n));
    for (size_t i = 0; i != m; ++i)
    {
        for (size_t j = 0; j != n; ++j)
        {
            std::cin >> matrix[i][j];
        }
    }
}

void stringprint()
{
    std::string line;
    std::getline(std::cin, line);
    for (char symbol : line)
    {
        std::cout << symbol << "\t" << static_cast<int>(symbol) << "\n";
    }
}

void somefoo()
{
somelabel:
    int a = 6, b = 4;
    double qq = a * 1.0 / b;
    double q = static_cast<double>(a) / b; // 1.5

    char c = 'A';
    c += 25;                // увеличиваем ASCII-код символа на 25
    std::cout << c << "\n"; // Z
    if (c == 'r')
    {
        goto somelabel;
    }

    auto x = 42;       // int
    auto pi = 3.14159; // double
}

void printsize()
{
    int8_t qwe;
    uint8_t wer;
    int16_t ert;
    uint16_t asd;
    int32_t fdf;
    uint32_t vcv;
    int64_t zdf;
    uint64_t hwe;
    unsigned int ui = 4294967295;                                // 2^32 - 1
    std::cout << "char: " << sizeof(char) << "\n";               //  1
    std::cout << "bool: " << sizeof(bool) << "\n";               //  1
    std::cout << "short int: " << sizeof(short int) << "\n";     //  2 (по стандарту >= 2)
    std::cout << "int: " << sizeof(int) << "\n";                 //  4 (по стандарту >= 2)
    std::cout << "long int: " << sizeof(long int) << "\n";       //  8 (по стандарту >= 4)
    std::cout << "long long int: " << sizeof(long long) << "\n"; //  8 (по стандарту >= 8)
    std::cout << "float: " << sizeof(float) << "\n";             //  4
    std::cout << "double: " << sizeof(double) << "\n";           //  8
    std::cout << "long double: " << sizeof(long double) << "\n"; // 16

    std::cout << "minimum value: " << std::numeric_limits<int>::min() << "\n"
              << "maximum value: " << std::numeric_limits<int>::max() << "\n";
}

void stroki()
{
    std::string s = "Some string";

    // приписывание символов и строк
    s += ' ';               // добавляем отдельный символ в конец, это аналог push_back
    s += "functions";       // добавляем строку в конец
    std::cout << s << "\n"; // Some string functions

    // выделение подстроки
    // подстрока "string" из 6 символов начиная с 5-й позиции
    std::string sub1 = s.substr(5, 6);
    // подстрока "functions" с 12-й позиции и до конца
    std::string sub2 = s.substr(12);

    // поиск символа или подстроки
    size_t pos1 = s.find(' ');           // позиция первого пробела, в данном случае 4
    size_t pos2 = s.find(' ', pos1 + 1); // позиция следующего пробела (11)
    size_t pos3 = s.find("str");         // вернётся 5

    // вставка подстроки
    s.insert(5, "std::");
    std::cout << s << "\n"; // Some std::string functions

    // замена указанного диапазона на новую подстроку
    s.replace(0, 4, "Special");
    std::cout << s << "\n"; // Special std::string functions

    // удаление подстроки
    s.erase(8, 5); // Special string functions

    std::string phrase;
    std::getline(std::cin, phrase);

    if (phrase.starts_with("hello"))
    {
        std::cout << "Greeting\n";
    }

    if (phrase.ends_with("bye"))
    {
        std::cout << "Farewell\n";
    }
}

enum class Color
{
    White,
    Red,
    Orange,
    Blue,
};

void Cvetta()
{
    Color c1 = Color::Blue;
    int value = static_cast<int>(c1);     // 3
    Color color3 = static_cast<Color>(2); // Color::Orange
}

struct Point
{
    double x = 0.0;
    double y = 0.0;
    double z = 0.0;
    Color color; // пусть у нас будет цветная точка
};

void tochk()
{
    Point point1; // по умолчанию координаты будут нулевыми, а color никак не будет проинициализирован
    point1.color = Color::Blue;

    Point point2 = {1.4, -2.2, -3.98, Color::Red};

    point2.z = 32; // обращаться к полям можно через точку
    point2.x += 2; // и вообще работать с ними как с обычными переменными

    Point point3 = {.x = 1.4, .y = -2.2, .z = -3.98};
    Point point4 = {.color = Color::Orange};

    std::cout << sizeof(double) << "\n"; // 8
    std::cout << sizeof(Color) << "\n";  // 4 (фактически это int)
    std::cout << sizeof(Point) << "\n";  // 32. https://en.cppreference.com/w/cpp/language/object#Alignment
}

void para()
{
    // в угловых скобках нужно указывать два типа:
    std::pair<int, double> p = {42, 3.14};

    // обращаться к полям можно через .first и .second:
    std::cout << p.first << "\n";  // 42
    std::cout << p.second << "\n"; // 3.14

    std::tuple<int, double, Point> t = {42, 3.14, {.color = Color::Orange}};

    // тут уже нет полей .first и .second,
    // но есть стандартная функция std::get<>,
    // которая принимает в угловых скобках индекс элемента (индексация с нуля):
    std::cout << std::get<0>(t) << "\n";   // 42
    std::cout << std::get<1>(t) << "\n";   // 3.14
    std::cout << std::get<2>(t).x << "\n"; // 0.0

    // вызов std::get может появляться и слева от присваивания:
    std::get<2>(t).color = Color::Red;

    std::pair<std::string, int> p = {"hello", 42};
    auto [word, freq] = p; // word = "hello"; freq = 42; https://en.cppreference.com/w/cpp/language/structured_binding
}

void stw()
{
    std::string s1 = "Elementary, my dear Watson!";
    std::string s2 = s1;

    s1.clear(); // s2 никак не изменится

    std::cout << s1 << "\n"; // пустая строка
    std::cout << s2 << "\n"; // Elementary, my dear Watson!
}

void lin()
{
    int x = 42;
    int &ref = x; // ссылка на x

    ++x;
    std::cout << ref << "\n"; // 43
}

void lin2()
{
    int x = 42, y = 13;
    int &ref = x; // OK
    ref = y;      // ссылка останется привязанной к x, значение x поменяется
}

struct Pot
{
    double x, y, z;
};

void poi()
{
    int x = 42;
    int *ptr = &x;             // сохраняем адрес в памяти переменной x в указатель ptr
    ++x;                       // увеличим x на единицу
    std::cout << *ptr << "\n"; // 43

    // address in memory
    int x = 1, y = 2, z = 3;
    std::cout << &x << "\n";
    std::cout << &y << "\n";
    std::cout << &z << "\n";

    int x = 42, y = 13;
    int *ptr;                  // по умолчанию не инициализируется, тут лежит «случайный» адрес
    ptr = nullptr;             // «нулевой» указатель
    ptr = &x;                  // теперь в ptr лежит адрес переменной x
    std::cout << *ptr << "\n"; // 42
    ptr = &y;                  // можно поменять адрес, записанный в ptr
    std::cout << *ptr << "\n"; // 13

    Pot pq = {3.0, 4.0, 5.0};
    Pot *pz = &pq;
    std::cout << (*pz).x << "\n"; // обращение через * и . требует скобок
    std::cout << pz->x << "\n";   // то же самое, но чуть короче
}

void cco()
{
    const int c1 = 42; // эта константа известна в compile time
    int x;
    std::cin >> x;
    const int c2 = 2 * x; // значение становится известным только в runtime
    // c2 = 0;  // ошибка компиляции: константе нельзя присвоить новое значение

    const std::vector<int> v = {1, 3, 5};
    std::cout << v.size() << "\n"; // OK, напечатает 3
    // v.clear();  // ошибка компиляции: константный вектор нельзя изменять
    // v[0] = 0;  // тоже ошибка компиляции
}

void copo()
{
    int x = 42;
    const int cx = 13;
    int *ptr = &x; // обычный указатель
    // ptr = &cx;  // ошибка компиляции
    const int *cptr = &x; // OK: через *cptr нельзя будет изменить x
    cptr = &cx;           // OK
    int *const ptrc = &x; // OK: *ptrc можно менять, но сам ptrc менять нельзя
    // ptrc = nullptr;  // ошибка компиляции
    const int *const cptrc = &x; // OK, для &cx тоже бы сработало
}

void uude()
{
    int *ptr = nullptr;
    {
        int x = 42;
        ptr = &x;
    }
    // обращаться к памяти, в которой жила переменная x, уже нельзя:
    std::cout << *ptr << "\n"; // неопределённое поведение!
}

std::uint64_t Factorial(std::uint64_t n)
{
    if (n == 0)
    {
        return 1;
    }
    return n * Factorial(n - 1); // рекурсивный вызов
}

void Swap(int &x, int &y)
{ // передаём аргументы по ссылке
    int z = x;
    x = y;
    y = z;
}

void f(const std::vector<int> &v)
{
    // Такой аргумент не требует дорогого копирования,
    // его нельзя случайно изменить внутри,
    // и такую функцию можно вызывать от констант!
}

struct Date
{
    int year = 1970;
    int month = 1;
    int day = 1;
};

bool operator<(const Date &lhs, const Date &rhs)
{
    if (lhs.year != rhs.year)
    {
        return lhs.year < rhs.year;
    }
    if (lhs.month != rhs.month)
    {
        return lhs.month < rhs.month;
    }
    return lhs.day < rhs.day;
}

// or via tie
// bool operator < (const Date& lhs, const Date& rhs) {
// return std::tie(lhs.year, lhs.month, lhs.day) < std::tie(rhs.year, rhs.month, rhs.day);
// }

bool CompareWithoutYear(const Date &lhs, const Date &rhs)
{
    return std::tie(lhs.month, lhs.day) < std::tie(rhs.month, rhs.day);
}

int Fdf()
{
    std::vector<Date> dates = {
        {2020, 3, 15},
        {2019, 1, 21},
        {2021, 1, 30}};
    // напечатаем содержимое:
    for (const auto &[year, month, day] : dates)
    {
        std::cout << year << "." << month << "." << day << "\n";
    }

    // or
    std::sort(dates.begin(), dates.end(), CompareWithoutYear);
}

void Vialambda()
{
    std::vector<Date> dates;
    std::sort(dates.begin(), dates.end(), [](const Date &lhs, const Date &rhs)
              { return std::tie(lhs.month, lhs.day) < std::tie(rhs.month, rhs.day); });
}

int CorrectCppCode()
{
    []() {}();
}

std::string Maxii(const std::string &x, const std::string &y)
{
    if (x > y)
    {
        return x;
    }
    else
    {
        return y;
    }
}

template <typename T>
T Max(const T &x, const T &y)
{
    if (x > y)
    {
        return x;
    }
    else
    {
        return y;
    }
}

// перегрузка для векторов
template <typename T>
const std::vector<T> &Max(const std::vector<T> &v1, const std::vector<T> &v2)
{
    if (v1.size() > v2.size())
    {
        return v1;
    }
    else if (v1.size() < v2.size())
    {
        return v2;
    }
    else if (v1 > v2)
    {
        return v1;
    }
    else
    {
        return v2;
    }
}

void arra()
{
    std::array<int, 3> point = {1, 2, 3};

    std::deque<int> d = {1, 2, 3, 4};
    d.push_back(5); // добавление в конец, как у вектора
    d.push_back(6);
    d.pop_back();    // удаление из конца, как у вектора
    d.push_front(0); // добавление в начало
    d.push_front(-1);
    d.pop_front(); // удаление из начала

    std::list<int> l = {10, 15, 20};
    l.push_front(5);
    l.push_front(0);
    l.push_back(25);
    l.push_back(30);
    l.pop_front();
    l.pop_back();
    for (int x : l)
    {
        std::cout << x << "\n"; // 5 10 15 20 25
    }

    // Используем auto, чтобы не писать громоздкий тип std::list<int>::iterator
    auto iter = l.begin();
    std::cout << *iter << "\n"; // печатаем начальный элемент
    ++iter;                     // сдвигаемся к следующему элементу
    --iter;                     // возвращаемся назад

    for (auto iter = l.begin(); iter != l.end(); ++iter)
    {
        std::cout << *iter << "\n"; // печатаем элементы списка через итератор
    }
    for (auto iter = l.rbegin(); iter != l.rend(); ++iter)
    {
        std::cout << *iter << "\n"; // проход по списку в обратном порядке
    }

    auto qwe = l.begin();
    ++qwe;
    l.insert(qwe, 5); // вставляем на эту позицию элемент
    // Удаляем из списка чётные числа
    for (auto qwe = l.begin(); qwe != l.end();)
    {
        if (*qwe % 2 == 0)
        {
            qwe = l.erase(qwe); // возвращается итератор на элемент, следующий за удалённым
        }
        else
        {
            ++qwe;
        }
    }
}

void iterr()
{
    std::list<int> l = {0, 10, 15, 20};
    // вместо этого удобнее воспользоваться функцией std::next:
    // auto iter = l.begin();
    // ++iter;
    auto iter = std::next(l.begin()); // итератор указывает на второй элемент списка
    std::advance(iter, 2);            // итератор указывает на последний элемент
}

void odnosv()
{
    std::forward_list<int> fl = {3, 42, 5};
    fl.push_front(2);
    // fl.push_back(10);  // ошибка компиляции!

    auto iter = std::next(fl.begin());
    iter = fl.erase_after(iter);
    fl.insert_after(iter, 4);

    for (int x : fl)
    {
        std::cout << x << "\n"; // 2 3 5 4
    }
}

void mmma()
{
    // инициализируем map набором пар {ключ, значение}
    std::map<std::string, int> years = {
        {"Moscow", 1147},
        {"Rome", -753},
        {"London", 47},
    };

    for (const auto &[city, year] : years)
    {
        std::cout << city << ": " << year << "\n";
    }
}

void mmapp()
{
    std::map<std::string, int> data;
    std::string key;
    int value;

    while (std::cin >> key >> value)
    {
        data[key] = value; // вставка
    }

    data.erase("hello"); // удаление

    // поиск
    if (auto iter = data.find("test"); iter != data.end())
    {
        std::cout << "Found the key " << iter->first << " with the value " << iter->second << "\n";
    }
    else
    {
        std::cout << "Not found\n";
    }
}

void unma()
{
    std::unordered_map<std::string, int> freqs;
    std::string word;
    while (std::cin >> word)
    {
        ++freqs[word];
    }
    for (const auto &[word, freq] : freqs)
    {
        std::cout << word << "\t" << freq << "\n";
    }
}

void unma2()
{
    std::unordered_map<std::string, int> freqs;
    freqs.reserve(300'000); // можно использовать апостроф для выделения разрядов
    std::string word;
    while (std::cin >> word)
    {
        ++freqs[word];
    }
    for (const auto &[word, freq] : freqs)
    {
        std::cout << word << "\t" << freq << "\n";
    }
}

void macop()
{
    std::unordered_map<std::string, int> freqs;
    std::string word;
    while (std::cin >> word)
    {
        ++freqs[word];
    }

    // копируем пары в вектор, используя шаблонный конструктор от двух итераторов:
    std::vector<std::pair<std::string, int>> sortedByFreq(
        freqs.begin(),
        freqs.end());

    // сортируем с помощью своей лямбда-функции:
    std::sort(
        sortedByFreq.begin(),
        sortedByFreq.end(),
        [](const auto &p1, const auto &p2)
        {
            // сначала сравниваем частоты по убыванию, потом — слова по возрастанию
            return std::tie(p2.second, p1.first) < std::tie(p1.second, p2.first);
        });

    for (const auto &[word, freq] : sortedByFreq)
    {
        std::cout << word << "\t" << freq << "\n";
    }
}

void ssee()
{
    std::unordered_set<std::string> words;
    std::set<std::string> duplicate_words;
    std::string word;
    while (std::cin >> word)
    {
        auto [iter, has_been_inserted] = words.insert(word);
        if (!has_been_inserted)
        {
            duplicate_words.insert(word);
        }
    }
    for (const auto &word : duplicate_words)
    {
        std::cout << word << "\n";
    }
}

void wrr()
{
    std::map<int, std::string> numbers = {
        {100, "hundred"},
        {3, "three"},
        {42, "forty two"},
        {11, "eleven"},
    };

    auto iter = numbers.find(11);

    if (iter != numbers.end())
    {
        // печатаем найденный элемент
        const auto &[key, value] = *iter;
        std::cout << "Found: " << key << ": " << value << "\n"; // Found: 11: eleven

        // печатаем предыдущий элемент
        if (iter != numbers.begin())
        {
            const auto &[key, value] = *std::prev(iter);
            std::cout << "Previous: " << key << ": " << value << "\n"; // Previous: 3: three
        }
        else
        {
            std::cout << "No previous element\n";
        }

        // печатаем следующий элемент
        if (auto nextIter = std::next(iter); nextIter != numbers.end())
        {
            const auto &[key, value] = *nextIter;
            std::cout << "Next: " << key << ": " << value << "\n"; // Next: 42: forty two
        }
        else
        {
            std::cout << "No next element\n";
        }
    }
    else
    {
        std::cout << "Not found\n";
    }
}

void countelle()
{
    std::vector<int> v = {2, 7, 1, 8, 2, 8};
    std::cout << std::count(v.begin(), v.end(), 8) << "\n"; // 2
}

void ffii()
{
    std::deque<int> d = {3, 14, 15, 92, 6};

    // Такой элемент есть, мы его точно найдём
    auto iter1 = std::find(d.begin(), d.end(), 15);
    // Итераторы дека можно вычитать, напечатается индекс найденного элемента
    std::cout << (iter1 - d.begin()) << "\n";

    auto start = d.begin();
    // К итераторам дека и вектора можно прибавлять целые числа
    auto end = start + 3;
    // Полуинтервал [start; end) теперь ограничивает подпоследовательность 3, 14, 15

    auto iter2 = std::find(start, end, 19);
    if (iter2 == end)
    {
        std::cout << "No such element!\n";
    }
    else
    {
        std::cout << *iter2 << "\n";
    }
    // Напечатает No such element
}

void couif()
{
    std::string s = "iPhone SE";

    std::cout << std::count_if(
                     s.begin(),
                     s.end(),
                     [](char c)
                     {
                         return 'A' <= c && c <= 'Z';
                     })
              << "\n"; // 3
}

template <typename Iter, typename Predicate>
Iter findas_if(Iter first, Iter last, Predicate p)
{
    while (first != last)
    {
        if (p(*first))
        { // применяем предикат
            return first;
        }
        ++first;
    }
    return last;
}

void revas()
{
    std::string s = "No lemon, no melon!";
    std::reverse(s.begin(), s.end());
    std::cout << s << "\n"; // !nolem on ,nomel oN
}

template <typename Iter>
void reversemy(Iter first, Iter last)
{
    while (first != last)
    {
        --last;
        if (first == last)
        {
            break;
        }
        std::swap(*first, *last);
        ++first;
    }
}

void disti()
{
    std::vector<int> v = {5, 5, 3, 2, 2, 5, 9, 1};
    auto iter = std::unique(v.begin(), v.end());
    // В векторе окажется 5, 3, 2, 5, 9, 1, 9, 1
    //                                      ^ iter будет указывать сюда
    v.erase(iter, v.end()); // удаляем хвост из ненужных элементов
}

/*
Среди других алгоритмов, модифицирующих последовательность, имеются

std::fill и std::generate (заполнение заданными значениями),
std::rotate (циклический сдвиг),
std::replace (замена элементов),
std::remove (переупорядочивание элементов, чтобы указанный элемент не встречался в начале),
std::shuffle (случайная перестановка элементов).
*/

void errr() {
    std::vector<int> v = {3, 14, 15, 92, 6};
    std::list<int> l;
    l.resize(v.size());  // теперь в списке l 5 нулей
    std::copy(v.rbegin(), v.rend(), l.begin());

    for (int x : l) {
        std::cout << x << " ";
    }
    std::cout << "\n";  // 6 92 15 14 3
}