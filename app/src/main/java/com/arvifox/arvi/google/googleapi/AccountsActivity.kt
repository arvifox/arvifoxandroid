package com.arvifox.arvi.google.googleapi

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.databinding.ActivityAccountsBinding
import com.google.android.material.snackbar.Snackbar

class AccountsActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, AccountsActivity::class.java)
        }
    }

    private lateinit var binding: ActivityAccountsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.includa.toolbar)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        val am = AccountManager.get(this)
        val acs = am.accounts
        val sb = StringBuilder()
        for (ac in acs) {
            sb.append(ac?.name).append(" : ").append(ac?.type).append("\n")
        }
        binding.tvAccounts.text = sb.toString()
    }
}
