package com.example.mangiaebasta.model.dataClasses

import android.location.Location
import kotlinx.serialization.Serializable

typealias Timestamp = String

@Serializable
data class ResponseError(
    val message: String
)

@Serializable
data class APILocation(
    val lat: Double,
    val lng: Double
)

fun Location.toAPILocation(): APILocation {
    return APILocation(this.latitude, this.longitude)
}

data class Error(
    val title: String = "An Unexpected Error Occurred",
    override val message: String,
) : Exception(message)