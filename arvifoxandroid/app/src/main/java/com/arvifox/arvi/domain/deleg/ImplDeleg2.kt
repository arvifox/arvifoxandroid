package com.arvifox.arvi.domain.deleg

object del {

    class ShapeShifter(copied: Human) : WeightLifter, Human by copied {
        override fun liftHeavyStuff() {
            println("a")
        }

        override fun pose() {
            println("v")
        }
    }

    interface Human {
        fun eat()
        fun sleep()
        fun poop()
    }

    interface WeightLifter : Human {
        fun liftHeavyStuff()
        fun pose()
    }
}