package com.example.mangiaebasta.model.dataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class Menu(
    val mid: Int,
    val name: String,
    val price: Float,
    val location: APILocation,
    val imageVersion: Int,
    val shortDescription: String,
    val deliveryTime: Int
)

@Serializable
data class MenuDetails(
    val mid: Int,
    val name: String,
    val price: Float,
    val location: APILocation,
    val imageVersion: Int,
    val shortDescription: String,
    val deliveryTime: Int,
    val longDescription: String,
)

@Serializable
data class MenuImage(
    var base64: String,
)

@Serializable
data class MenuDetailsWithImage(
    val menuDetails: MenuDetails,
    val image: MenuImage
)

@Serializable
data class MenuWithImage(
    val menu: Menu,
    val image: MenuImage? = null
)

@Entity
data class MenuImageWithVersion(
    @PrimaryKey val mid: Int,
    val imageVersion: Int,
    val image: String
)