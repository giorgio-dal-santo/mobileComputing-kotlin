package com.example.mangiaebasta.model.dataSource

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mangiaebasta.model.dataClasses.MenuImageWithVersion


@Dao
interface MenuImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuImage(newData: MenuImageWithVersion)

    @Query("SELECT * FROM MenuImageWithVersion WHERE mid = :mid AND imageVersion = :imageVersion LIMIT 1")
    suspend fun getMenuImageByVersion(mid: Int, imageVersion: Int): MenuImageWithVersion?

}

@Database(entities = [MenuImageWithVersion::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuImageDao(): MenuImageDao
}


class DBController(
    context: Context
) {

    private val database = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "images-database"
    ).build()

    val dao = database.menuImageDao()

}