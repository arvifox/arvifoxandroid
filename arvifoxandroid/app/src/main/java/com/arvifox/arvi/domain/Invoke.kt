package com.arvifox.arvi.domain

object sdqwf {

    class MyClass<T>(
        //private val myValue: T = 10 // can't give a default value to a generic member
    ) {
    }

}

object sssdf {
    class MyClass<T>(
        private val myValue: T
    ) {

        companion object {
            operator fun invoke() = MyClass(10) // this can be invoked as MyClass()
        }
    }

    fun sdf() {
        val d = MyClass.invoke()
    }
}

object erwr {

}