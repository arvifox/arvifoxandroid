package com.arvifox.arvi.roomtest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arvifox.arvi.simplemisc.room.User
import com.arvifox.arvi.simplemisc.room.UserDao
import com.arvifox.arvi.simplemisc.room.UsersDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomTest {

    private lateinit var db: UsersDatabase
    private lateinit var dao: UserDao

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, UsersDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGet() = runBlocking {
        val u = User(userName = "username")
        dao.insert(u)
        val all = dao.getUsersFlow().first()
        assertEquals(all[0].userName, u.userName)
    }
}