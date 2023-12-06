package com.arvifox.arvi.domain.arccom

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

//https://developer.android.com/jetpack/androidx/releases/room

@Entity
data class Dog(
    @PrimaryKey val name: String,
    val cuteness: Int,
    val barkingVolume: Int
)

interface dao {
    @Query("SELECT * FROM Dog")
    fun getAllDogs(): List<Dog>

    @Query("SELECT * FROM Dog")
    fun getAllDogsF(): Flow<List<Dog>>

    @Query("SELECT * FROM Dog WHERE name = :name")
    fun getDog(name: String): Flow<Dog>
}

@Dao
abstract class DoggosDao {
    @Query("SELECT * FROM Dog WHERE name = :name")
    abstract fun getDog(name: String): Flow<Dog>
    fun getDogDistinctUntilChanged(name:String) =
        getDog(name).distinctUntilChanged()
}