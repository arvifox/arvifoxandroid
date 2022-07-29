package com.arvifox.arvi.disp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/*
https://proandroiddev.com/companion-object-invoke-operator-overloading-for-default-constructor-argument-in-generic-classes-c8eb61dcc4f7
 */

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}

@ExperimentalCoroutinesApi
class CoroutineRule<T : CoroutineDispatcher>(
    val dispatcher: T
) : TestWatcher() {

    companion object {
        operator fun invoke() = CoroutineRule(Dispatchers.Unconfined)
    }

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        (dispatcher as? TestCoroutineDispatcher)?.cleanupTestCoroutines()
    }
}