package com.example.mangiaebasta.data.dataSource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mangiaebasta.data.dataSource.room.MenuImageDao

@Database(entities = [MenuImageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuImageDao(): MenuImageDao
}