package com.arvifox.arvi.navig

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.arvifox.arvi.R

class NavigActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, NavigActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navig)
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host_fragment).navigateUp()

    fun getNavigator(): NavController {
        return NavHostFragment.findNavController(supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!)
//        return Navigation.findNavController(this, R.id.nav_host_fragment)
//        return Navigation.findNavController(findViewById(R.id.nav_host_fragment))
    }
}
