package com.arvifox.arvi.domain.corou

import java.util.Objects

class Foot(var goals: Long, val who: String)

val feet = mutableListOf(1, 3, 5)
val teams = HashSet<Foot>()

fun letsplay() {
    val team1 = Foot(2, "alien")
    val team2 = Foot(2, "alien")

    teams.add(team1)
    teams.add(team2)

    feet.add(7)
    feet.loopFor {
        if (it == 3) {
            println("wow 3")
//            return
            return@loopFor
        }
        println("just $it")
    }

    println(
        "teams ${team1 === team2} ${team1 == team2} ${
            Objects.equals(
                team1,
                team2,
            )
        } ${team1.hashCode()} ${team2.hashCode()}",
    )
    println("teams: ${teams.size} teams")
    println("finish!")
}

inline fun <reified T> List<T>.loopFor(action: (T) -> Unit) {
    this@loopFor.reversed().forEach {
        action(it)
    }
}
