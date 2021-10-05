package com.arvifox.arvi.simplemisc.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM Users WHERE userid = :id ORDER BY username ASC")
    suspend fun getUserById(id: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM Users")
    suspend fun deleteAllUsers()

    @Query("SELECT * FROM Users ORDER BY username ASC")
    fun getUsersFlow(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(u: User): Long
}
