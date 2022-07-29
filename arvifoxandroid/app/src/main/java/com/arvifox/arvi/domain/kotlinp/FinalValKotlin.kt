package com.arvifox.arvi.domain.kotlinp

/**
 * [https://nuancesprog.ru/p/12970/]
 */
class FinalBlog {
    val someProperty: String = "some"
    init {
        print(someProperty + "thing")
    }
}

open class FinalBlog2 {
    open val someProperty: String ="some"
    init {
        print(someProperty + "thing")
    }
}

//---------

interface BlogTopic {
    val someProperty: String
}

open class FinalBlog3: BlogTopic {
    final override val someProperty: String = "some"
    init {
        print(someProperty + "thing")
    }
}
