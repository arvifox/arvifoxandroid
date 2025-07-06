#include <iostream>
#include <string>
#include <limits>
#include <cmath>
using namespace std;
int main()
{
    int a, b;
    std::cin >> a >> b;
    bool vis;
    if (a % 400 == 0)
    {
        vis = true;
    }
    else if (a % 100 == 0)
    {
        vis = false;
    }
    else if (a % 4 == 0)
    {
        vis = true;
    }
    else
    {
        vis = false;
    }
    switch (a)
    {
    case 1:
        std::cout << "31";
        break;
    case 2:
        if (vis)
        {
            std::cout << "29";
        }
        else
        {
            std::cout << "28";
        }
        break;
    case 3:
        std::cout << "31";
        break;
    case 4:
        std::cout << "30";
        break;
    case 5:
        std::cout << "31";
        break;
    case 6:
        std::cout << "30";
        break;
    case 7:
        std::cout << "31";
        break;
    case 8:
        std::cout << "31";
        break;
    case 9:
        std::cout << "30";
        break;
    case 10:
        std::cout << "31";
        break;
    case 11:
        std::cout << "30";
        break;
    case 12:
        std::cout << "31";
        break;
    default:
        break;
    }
}

void ff(int year)
{
    if ((year % 400 == 0 || year % 100 != 0) && year % 4 == 0)
    {
        std::cout << "29\n";
    }
    else
    {
        std::cout << "28\n";
    }
}
