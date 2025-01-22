package com.example.mangiaebasta.model.dataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class Menu (
    val id : Int,
    val name : String,
    val price : Float,
    val location : APILocation,
    val imageVersion : Int,
    val shortDescription : String,
    val deliveryTime : Int
)

@Serializable
data class MenuDetails (
    val id : Int,
    val name : String,
    val price : Float,
    val location : APILocation,
    val imageVersion : Int,
    val shortDescription : String,
    val deliveryTime : Int,
    val longDescription : String,
)

@Serializable
data class MenuImage (
    val string64 : String,
)

@Entity
data class MenuImageWithVersion(
    @PrimaryKey val mid: Int,
    val imageVersion: Int,
    val image: String
)