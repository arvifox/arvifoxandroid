package jp.co.soramitsu.myapplication

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel : ViewModel() {

    private val cm: NetworkStateListener by lazy {
        NetworkStateListener()
    }

    fun davay(c: Context): StateFlow<NetworkStateListener.State> {
        Log.e("foxx","davay")
        return cm.subscribe(c)
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                NetworkStateListener.State.DISCONNECTED,
            )
    }
}
