package com.arvifox

import kotlin.math.abs

// Допустим, мы устроили чемпионат по шагам,
// и теперь хотим автоматически подвести результаты.

// Необходимо определить userIds участников, которые прошли наибольшее количество
// шагов steps за все дни, не пропустив ни одного дня соревнований.

// ## Пример 1

// # ввод
// statistics = [
//     [{ userId: 1, steps: 1000 }, { userId: 2, steps: 1500 }],
//     [{ userId: 2, steps: 1000 }]
// ]

// # вывод
// champions = { userIds: [2], steps: 2500 }

// ## Пример 2

// # ввод
// statistics = [
//     [{ userId: 1, steps: 2000 }, { userId: 2, steps: 1500 }],
//     [{ userId: 2, steps: 4000 }, { userId: 1, steps: 3500 }]
// ]

// # вывод
// champions = { userIds: [1, 2], steps: 5500 }

// N - количество дней
// M — количество юзеров

class UserDay(val id: Int, val steps: Int)

fun main(days: List<List<UserDay>>) {
    val resultMap = mutableMapOf<Int, Int>()
    days.forEachIndexed { dayIndex, day ->
        day.forEach { userDay ->
            val ud = resultMap[userDay.id]
            if (ud != null) {
                resultMap[userDay.id] = resultMap[userDay.id]!! + userDay.steps
            } else {
                if (dayIndex == 0) {
                    resultMap[userDay.id] = userDay.steps
                }
            }
        }

        val keysOfDay: Map<Int, UserDay> = day.associateBy { it.id }
        resultMap.removeAllBy { keysOfDay[it] == null }
    }

    //val sorted = resultMap.toList { UserDay(it.key, it.value) }.sortedBy { it.steps }
    val resultList = resultMap.toList()
    val maxSteps = resultList.maxOf { it.second }
    // var i = sorted.lastIndex
    // val max = sorted[sorted.lastIndex].steps
    val maxIds = mutableListOf<Int>()
    // while (i >= 0) {
    //     if (sorted[i].steps == max) {
    //         maxIds.add(sorted[i].id)
    //     } else {
    //         break
    //     }
    //     i--
    // }
    resultList.forEach {
        if (it.second == maxSteps) {
            maxIds.add(it.first)
        }
    }
    println(maxIds)
    println(maxSteps)
}

// по времени O(N*M + M) = O((N * M)
// по памяти O(M)

// =============== ЗАДАЧА 2 ===============

// ## Условие задачи
// Есть множество товаров.

// Допустим, каждый из них представлен числом, а у каждого покупателя есть потребность
// в товаре, также выраженная числом.

// Если для покупателя нет точного совпадения его потребности с товаром, он выбирает
// ближайший по значению товар, что вызывает неудовлетворённость, равную разнице между
// его потребностью и купленным товаром.

// Количество каждого товара не ограничено, и один товар могут купить несколько покупателей.

// Рассчитайте суммарную неудовлетворённость всех покупателей.

// ## Пример

// # ввод
// goods = [8, 3, 5]
// buyerNeeds = [5, 6]

// # вывод
// res = 1

// первый покупатель покупает товар 5 и его неудовлетворённость = 0,
// второй хочет 6, но покупает товар 5 и его неудовлетворённость = 6-5 = 1

// N товаров
// M покупателей

fun main(goods: List<Int>, buyerNeeds: List<Int>) {
    val goodsSorted = goods.sortedBy { it }
    var sum = 0
    buyerNeeds.forEach { need ->
        var delta = Int.MAX_VALUE
        var i = 0
        while (i <= goodsSorted.lastIndex) {
            val curDelta = abs(need - goodsSorted[i])
            if (curDelta <= delta) {
                delta = curDelta
            } else {
                break
            }
            i++
        }
        sum += delta
    }
    println(sum)
}

// по времени O(N*logN + N*M)
// по памяти O(1)

private fun <K, V> MutableMap<K, V>.removeAllBy(predicate: (K) -> Boolean) {
    val keysToRemove = mutableListOf<K>()
    this.forEach { entry ->
        if (predicate(entry.key)) {
            keysToRemove.add(entry.key)
        }
    }
    keysToRemove.forEach { key ->
        this.remove(key)
    }
}