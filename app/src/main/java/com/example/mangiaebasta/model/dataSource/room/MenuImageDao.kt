package com.example.mangiaebasta.model.dataSource.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mangiaebasta.model.dataSource.room.MenuImageEntity

@Dao
interface MenuImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuImage(menuImage: MenuImageEntity)

    @Query("SELECT image FROM MenuImageEntity WHERE mid = :mid AND imageVersion = :imageVersion")
    suspend fun getImageByImageVersion(mid: Int, imageVersion: Int): String?
}