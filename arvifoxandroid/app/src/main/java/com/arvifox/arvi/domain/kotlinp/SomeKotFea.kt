package com.arvifox.arvi.domain.kotlinp

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.io.path.Path
import kotlin.io.path.readBytes
import kotlin.properties.Delegates
import kotlin.random.Random
import kotlin.time.TimeSource
import kotlin.time.TimeSource.Monotonic.markNow
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalEncodingApi::class, ExperimentalStdlibApi::class)
object SomeKotFea {

    val e = 1755
    val es = e.toHexString()
    val eh = "somefee"
    val ehd = eh.hexToByteArray()

    val buis = "some string ${Typography.copyright} after ${Typography.nbsp} in quote ${Typography.leftDoubleQuote} ${Typography.rightDoubleQuote}"

    val bb = Path("/some/path/foo/where").readBytes()
    val bbenc = Base64.Default.encode(bb)
    val bbencurl = Base64.UrlSafe.encode(bb)
    val bbencmi = Base64.Mime.encode(bb)

    var obs: String by Delegates.observable("init") { p, o, n ->
        println("property $p changed from $o to $n")
    }
    var cet: String by Delegates.vetoable("init") { p, o, n ->
        println("property $p changed from $o to $n")
        n.length > 7
    }

    fun time1() {
        val l1 = System.currentTimeMillis()
        Thread.sleep(3400)
        val l2 = System.currentTimeMillis()
        val d = l2 - l1
    }

    fun time2() {
        val d = measureTime {
            Thread.sleep(3800)
        }
        println("taken $d")
    }

    fun time3() {
        val (v, d) = measureTimedValue {
            Thread.sleep(2399)
            2
        }
        println("taken ${d.toIsoString()}")
    }

    fun time4() {
        val m = mutableListOf<TimeSource.Monotonic.ValueTimeMark>()
        var i = 0
        while (true) {
            i += Random.nextInt()
            if (i > 1234) break
            m.add(markNow())
        }
    }
}