package com.example.mangiaebasta.model.dataClasses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class OrderStatus(orderStatus: String) {
    ON_DELIVERY("ON_DELIVERY"),
    COMPLETED("COMPLETED"),
}

@Serializable
data class Order(
    val oid: Int,
    val mid: Int,
    val uid: Int,
    val creationTimestamp: Timestamp,
    val status: OrderStatus,
    val deliveryLocation: APILocation,
    val deliveryTimestamp: Timestamp? = null,
    val expectedDeliveryTimestamp: Timestamp? = null,
    val currentPosition: APILocation,
)

@Serializable
data class BuyOrderRequest(
    @SerialName("sid") val sid: String,
    @SerialName("deliveryLocation") val deliveryLocation: APILocation
)