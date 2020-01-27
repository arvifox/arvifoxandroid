package com.arvifox.arvi.domain.inlinekotlin

/*
https://medium.com/androiddevelopers/zero-cost-abstractions-in-kotlin-19b953f3a936
 */

inline class DogId(val id: Long)

object Inline {
    class Dog(val id: DogId)
}

interface Id
inline class DoggoId(val id: Long) : Id {

    val stringId
        get() = id.toString()

    fun isValid() = id > 0L
}

object lsjdf {
}