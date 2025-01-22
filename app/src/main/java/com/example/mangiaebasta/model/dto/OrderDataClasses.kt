package com.example.mangiaebasta.model.dto

import kotlinx.serialization.Serializable

enum class OrderStatus(orderStatus: String) {
    ON_DELIVERY("ON_DELIVERY"),
    COMPLETED("COMPLETED"),
}

@Serializable
data class Order(
    val id: Int,
    val menuId: Int,
    val userId: Int,
    val status: OrderStatus,
    val creationTimestamp: Timestamp,
    val deliveryTimestamp: Timestamp? = null,
    val expectedDeliveryTimestamp: Timestamp? = null,
    val deliveryLocation: APILocation,
    val currentLocation: APILocation,
)