package com.arvifox.arvi.domain.deleg

class GeneticExperiment(human: Human, animal: Animal) : Human by human, Animal by animal {

    fun dslfj() {
        eat()

        bite()
    }


}

interface Hyhh {
    fun sdfs()
}

interface Human {
    fun eat()
    fun sleep()
    fun poop()
}

interface Animal {
    fun bite()
}

fun ksjdf() {
    val gg = GeneticExperiment(object :
        Human {
        override fun eat() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun sleep() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun poop() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }, object : Animal {
        override fun bite() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    })
}