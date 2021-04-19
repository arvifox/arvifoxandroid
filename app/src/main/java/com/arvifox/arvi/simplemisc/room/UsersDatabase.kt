package com.arvifox.arvi.simplemisc.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * [https://medium.com/androiddevelopers/using-and-testing-room-kotlin-apis-4d69438f9334]
 * [https://developer.android.com/training/data-storage/room]
 * [https://developer.android.com/codelabs/android-room-with-a-view-kotlin]
 */
@Database(entities = [User::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: UsersDatabase? = null

        fun getInstance(context: Context): UsersDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        UsersDatabase::class.java, "Sample.db")
                        .build()
    }
}
