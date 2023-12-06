package com.arvifox.paromte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.addRepeatingJob
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.paging.cachedIn
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arvifox.paromte.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn

@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
        private set

    private val vm: FoViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                @Suppress("UNCHECKED_CAST")
                return FoViewModel(FoRepo(), handle) as T
            }
        }
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        FoAdap()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDaba(applicationContext)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvList.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        addRepeatingJob(Lifecycle.State.STARTED) {
            vm.asd.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}

@ExperimentalPagingApi
class FoViewModel(
    re: FoRepo,
    saved: SavedStateHandle,
) : ViewModel() {

    val asd = re.getflow().cachedIn(viewModelScope).stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
}

class FoAdap : PagingDataAdapter<FoEntity, FoViolde>(dif) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoViolde =
        FoViolde(LayoutInflater.from(parent.context).inflate(R.layout.foitem, parent, false))

    override fun onBindViewHolder(holder: FoViolde, position: Int) =
        holder.bind(getItem(position)?.name ?: "no tut")
}

class FoViolde(v: View) : RecyclerView.ViewHolder(v) {
    private val tt = v.findViewById<TextView>(R.id.tvItem)
    fun bind(t: String) {
        tt.text = t
    }
}

val dif = object : DiffUtil.ItemCallback<FoEntity>() {
    override fun areContentsTheSame(oldItem: FoEntity, newItem: FoEntity): Boolean =
        oldItem == newItem

    override fun areItemsTheSame(oldItem: FoEntity, newItem: FoEntity): Boolean =
        oldItem.name == newItem.name
}