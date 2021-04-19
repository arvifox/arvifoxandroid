package com.arvifox.arvi.simplemisc.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable

import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM Users WHERE userid = :id ORDER BY username ASC")
    fun getUserById(id: String): Flowable<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Completable

    @Query("DELETE FROM Users")
    fun deleteAllUsers()

    @Query("SELECT * FROM Users WHERE userid = :id ORDER BY username ASC")
    fun getUsersFlow(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(u: User)
}
