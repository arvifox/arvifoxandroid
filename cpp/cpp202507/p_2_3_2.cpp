#include <iostream>
#include <string>
#include <limits>
#include <cmath>
int main()
{
    int a,b,c;
    std::cin >> a >> b >> c;
    if (a+b>c && a+c>b && b+c>a)
    {
        if (a*a+b*b==c*c || a*a+c*c==b*b || c*c+b*b==a*a)
        {
            std::cout << "YES";
        } else {
            std::cout << "NO";
        }
        
    } else {
        std::cout << "UNDEFINED";
    }
}
