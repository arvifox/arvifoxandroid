package com.arvifox.arvi.domain.strconeg

interface ConegStorage {

    interface Key<E : Item>

    interface Item : ConegStorage {
        val key: Key<*>
        operator fun <E : Item> get(key: Key<E>): E? = if (this.key == key) this as E else null
    }
}

class ConegDo : ConegStorage.Item {

    companion object Key : ConegStorage.Key<ConegDo>

    override val key: ConegStorage.Key<*> = ConegDo
}

fun main() {
    val cs: ConegStorage
}
