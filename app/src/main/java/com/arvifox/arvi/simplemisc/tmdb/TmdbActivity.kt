package com.arvifox.arvi.simplemisc.tmdb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.arvifox.arvi.databinding.ActivityTmdbBinding

class TmdbActivity : AppCompatActivity() {

    private lateinit var tmdbViewModel: TmdbViewModel

    private lateinit var binding: ActivityTmdbBinding

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, TmdbActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTmdbBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.incApBaLa.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tmdbViewModel = ViewModelProviders.of(this).get(TmdbViewModel::class.java)
        tmdbViewModel.fetchMovies()
        tmdbViewModel.popularMoviesLiveData.observe(this, Observer {
            //
        })
    }

    override fun onPause() {
        tmdbViewModel.cancelAllRequests()
        super.onPause()
    }
}
