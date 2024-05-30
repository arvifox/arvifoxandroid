package com.arvifox.arvi.domain.multithre

import java.util.concurrent.locks.ReentrantLock

class Relock {
    private val lock = ReentrantLock()
    private var count = 0

    fun increment() {
        lock.lock()
        try {
            count++
        } finally {
            lock.unlock()
        }
    }

    fun getCount(): Int {
        lock.lock()
        try {
            return count
        } finally {
            lock.unlock()
        }
    }
}