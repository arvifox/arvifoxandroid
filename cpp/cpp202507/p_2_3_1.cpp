#include <iostream>
#include <string>
#include <limits>
#include <cmath>
int main()
{
    int c1, r1, c2, r2;
    std::cin >> c1 >> r1 >> c2 >> r2;
    bool res;
    if (c1 == c2)
    {
        res = true;
    }
    else if (r1 == r2)
    {
        res = true;
    }
    else if (std::abs(c1 - c2) == std::abs(r1 - r2))
    {
        res = true;
    }
    else
    {
        res = false;
    }
    std::string sres;
    if (res)
        sres = "YES";
    else
        sres = "NO";
    std::cout << sres;
}
