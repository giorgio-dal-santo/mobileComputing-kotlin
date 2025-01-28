package com.example.mangiaebasta.model.dataClasses

import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

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

object Timestamps {

    fun Timestamp.toMillis(): Long {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.parse(this)?.time ?: 0L
    }

    fun Timestamp.formatTimestamp(): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC")
        val date = format.parse(this) ?: return this
        val prettyFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return prettyFormat.format(date)
    }
}