package com.example.mangiaebasta.model.dataSource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mangiaebasta.model.dataSource.room.MenuImageDao

@Database(entities = [MenuImageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuImageDao(): MenuImageDao
}