package com.arvifox.paromte

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

var daa: FoDaba? = null

@Entity(tableName = "tablename")
data class FoEntity(@PrimaryKey val name: String)

@Dao
interface FoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFo(all: List<FoEntity>)

    @Query("select * from tablename")
    fun paging(): PagingSource<Int, FoEntity>

    @Query("delete from tablename")
    suspend fun clearFo()

    @Query("select count(name) from tablename")
    suspend fun couro(): Long
}

@Database(
    version = 1,
    entities = [
        FoEntity::class,
    ],
)
abstract class FoDaba : RoomDatabase() {

    companion object {
        private var instance: FoDaba? = null

        fun get(c: Context): FoDaba = instance ?: buildFodaba(c)

        private fun buildFodaba(c: Context): FoDaba {
            return Room.databaseBuilder(
                c.applicationContext,
                FoDaba::class.java,
                "fodaba.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun fodao(): FoDao
}

fun initDaba(c: Context) {
    daa = FoDaba.get(c)
}