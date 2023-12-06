package com.arvifox.arvi.simplemisc.room

import android.content.Context

object Injection {

    fun provideUserDataSource(context: Context): UserDao {
        val database = UsersDatabase.getInstance(context)
        return database.userDao()
    }
}
