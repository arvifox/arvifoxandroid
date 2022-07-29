package com.arvifox.arvi.domain.corou

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

// GlobalScope example
class MainFragment : Fragment() {
    fun loadData() = GlobalScope.launch { }
}

// CoroutineScope example
class MainFragment2 : Fragment() {

    val uiScope = CoroutineScope(Dispatchers.Main)

    fun loadData() = uiScope.launch { }
}

// Fragment implements CoroutineScope example
class MainFragment3 : Fragment(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    fun loadData() = launch { }
}