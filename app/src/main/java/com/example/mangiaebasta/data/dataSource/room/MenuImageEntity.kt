package com.example.mangiaebasta.data.dataSource.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MenuImageEntity(
    @PrimaryKey val mid: Int,
    val imageVersion: Int,
    val image: String
)
