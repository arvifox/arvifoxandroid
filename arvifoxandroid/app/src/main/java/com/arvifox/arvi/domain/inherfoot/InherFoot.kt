package com.arvifox.arvi.domain.inherfoot

object InherFoot {

}

interface Fooba {
    val name: String
    fun scoreTo(temoo: Temoo) = println("fooba '${this.name}' score to temoo ${temoo.name} ")
}

interface Temoo {
    val name: String
}

class Juventus(override val name: String) : Temoo
class ManUnited(override val name: String) : Temoo
class RealMadrid(override val name: String) : Temoo

class Itagu(override val name: String) : Fooba
class Fragu(override val name: String) : Fooba
object Cristinka : Fooba {
    override val name: String = "Cristiano Ronaldo"
}

fun main() {
    val foobas = listOf<Fooba>()
}