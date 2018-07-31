package com.arvifox.arvi.google.arcorevision

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.arvifox.arvi.R

import kotlinx.android.synthetic.main.activity_accounts.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class AccountsActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, AccountsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
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
        tvAccounts.text = sb.toString()
    }
}
