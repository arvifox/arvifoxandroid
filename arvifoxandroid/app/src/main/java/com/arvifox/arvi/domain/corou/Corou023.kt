package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.random.Random

object Corou023 {

    fun main() = runBlocking {
        var sharedCounter = 0
        val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool")) // We want our code to run on 4 threads
        scope.launch {
            val coroutines = 1.rangeTo(1000).map { //create 1000 coroutines (light-weight threads).
                launch {
                    for(i in 1..1000){ // and in each of them, increment the sharedCounter 1000 times.
                        sharedCounter++
                    }
                }
            }

            coroutines.forEach {
                    corotuine->
                corotuine.join() // wait for all coroutines to finish their jobs.
            }
        }.join()

        println("The number of shared counter should be 1000000, but actually is $sharedCounter")
    }

    class Incrementor() {
        var sharedCounter: Int = 0
            private set

        fun updateCounterIfNecessary(shouldIActuallyIncrement: Boolean) {
            if (shouldIActuallyIncrement) {
                synchronized(this) {
                    //only locks when needed, using the Incrementor`s instance as the lock.
                    sharedCounter++
                }
            }
        }
    }
    fun main2() = runBlocking {
        val incrementor = Incrementor()
        val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool")) // We want our code to run on 4 threads
        scope.launch {
            val coroutines = 1.rangeTo(1000).map {
                //create 1000 coroutines (light-weight threads).
                launch {
                    for (i in 1..1000) { // and in each of them, increment the sharedCounter 1000 times.
                        incrementor.updateCounterIfNecessary(it % 2 == 0)
                    }
                }
            }
            coroutines.forEach { corotuine ->
                corotuine.join() // wait for all coroutines to finish their jobs.
            }
        }.join()
        println("The number of shared counter is ${incrementor.sharedCounter}")
    }
}

object Corou023a {
    class Incrementor() {
        val sharedCounter: AtomicInteger = AtomicInteger(0)

        fun updateCounterIfNecessary(shouldIActuallyIncrement: Boolean) {
            if (shouldIActuallyIncrement) {
                sharedCounter.incrementAndGet() // the increment (++) operation is done atomically, so all threads wait for its completion
            }
        }

        fun getSharedCounter():Int {
            return sharedCounter.get()
        }
    }

    fun main() = runBlocking {
        val incrementor = Incrementor()
        val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool")) // We want our code to run on 4 threads
        scope.launch {
            val coroutines = 1.rangeTo(1000).map {
                //create 1000 coroutines (light-weight threads).
                launch {
                    for (i in 1..1000) { // and in each of them, increment the sharedCounter 1000 times.
                        incrementor.updateCounterIfNecessary(it % 2 == 0)
                    }
                }
            }

            coroutines.forEach { corotuine ->
                corotuine.join() // wait for all coroutines to finish their jobs.
            }
        }.join()

        println("The number of shared counter is ${incrementor.getSharedCounter()}")
    }
}

object Corou023b {
    class Incrementor() {
        private val sharedCounterLock = ReentrantLock()
        var sharedCounter: Int = 0
            private set

        fun updateCounterIfNecessary(shouldIActuallyIncrement: Boolean) {
            if (shouldIActuallyIncrement) {
                try {
                    sharedCounterLock.lock()
                    sharedCounter++
                } finally {
                    sharedCounterLock.unlock()
                }
            }
        }
    }

    fun main() = runBlocking {
        val incrementor = Incrementor()
        val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool")) // We want our code to run on 4 threads
        scope.launch {
            val coroutines = 1.rangeTo(1000).map {
                //create 1000 coroutines (light-weight threads).
                launch {
                    for (i in 1..1000) { // and in each of them, increment the sharedCounter 1000 times.
                        incrementor.updateCounterIfNecessary(it % 2 == 0)
                    }
                }
            }

            coroutines.forEach { corotuine ->
                corotuine.join() // wait for all coroutines to finish their jobs.
            }
        }.join()

        println("The number of shared counter is ${incrementor.sharedCounter}")
    }
}

object Corou023c {
    class Incrementor() {
        private val sharedCounterLock = Semaphore(1)
        var sharedCounter: Int = 0
            private set

        fun updateCounterIfNecessary(shouldIActuallyIncrement: Boolean) {
            if (shouldIActuallyIncrement) {
                try {
                    sharedCounterLock.acquire()
                    sharedCounter++
                } finally {
                    sharedCounterLock.release()
                }
            }
        }
    }

    fun main() = runBlocking {
        val incrementor = Incrementor()
        val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool")) // We want our code to run on 4 threads
        scope.launch {
            val coroutines = 1.rangeTo(1000).map {
                //create 1000 coroutines (light-weight threads).
                launch {
                    for (i in 1..1000) { // and in each of them, increment the sharedCounter 1000 times.
                        incrementor.updateCounterIfNecessary(it % 2 == 0)
                    }
                }
            }

            coroutines.forEach { corotuine ->
                corotuine.join() // wait for all coroutines to finish their jobs.
            }
        }.join()

        println("The number of shared counter is ${incrementor.sharedCounter}")
    }
}

object Corou023d {
    fun main() {
        val createdValues = mutableListOf<Int>()
        /**
         *  create a cyclic barrier that waits for 3 threads to finish their jobs, and after that,
         *  prints the sum of all the values.
         */
        val cyclicBarrier = CyclicBarrier(3) {
            println("Sum of all values is ${createdValues.sum()}")
        }

        val threads = 1.rangeTo(3).map {
                number ->
            Thread {
                Thread.sleep(Random.nextInt(500).toLong())
                createdValues.add(number) //add a value to the list after 500ms
                cyclicBarrier.await()
                println("I am thread ${Thread.currentThread().name} and I finished my Job!")
            }.apply {
                start()
            }
        }
        threads.forEach { thread ->
            thread.join()
        }

    }
}

object Corou023e {
    fun main() {
        val createdValues = mutableListOf<Int>()
        /**
         *  create a countdown latch counts down from 5, and 3 threads await for the final countdown.
         *  after reaching zero, each thread prints their name.
         */
        val countDownLatch = CountDownLatch(5)

        val threads = 1.rangeTo(5).map {
                number ->
            Thread {
                createdValues.add(number)
                countDownLatch.countDown() //signal the CountDownLatch
            }.apply {
                start()
            }
        }
        /**
         * Let 3 threads wait and print the sum.
         */
        val waitingThreads = 1.rangeTo(3).map {
            Thread {
                countDownLatch.await()
                println("I'm thread ${Thread.currentThread().name} and the sum of all values is: ${createdValues.sum()}")
            }.apply {
                start()
            }
        }
        threads.forEach { thread ->
            thread.join()
        }
        waitingThreads.forEach { thread ->
            thread.join()
        }

    }
}

object Corou023f {
    // deadlock

    data class Human(val name:String) {
        @Synchronized fun sayHi(to: Human){
            println("$name saying hi to ${to.name}")
            Thread.sleep(500)
            to.sayHiBack(this)

        }
        @Synchronized fun sayHiBack(to: Human){
            println("$name saying hi back to ${to.name}")
        }

    }
    fun main() {
        val adam = Human("adam")
        val eve = Human("eve")
        val adamThread = Thread {
            adam.sayHi(eve)
        }.apply {
            start()
        }

        val eveThread = Thread {
            eve.sayHi(adam)
        }.apply {
            start()
        }
        adamThread.join()
        eveThread.join()
    }
}