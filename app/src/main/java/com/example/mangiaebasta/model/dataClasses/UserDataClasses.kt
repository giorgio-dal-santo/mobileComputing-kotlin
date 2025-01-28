package com.example.mangiaebasta.model.dataClasses

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val sid: String,
    val uid: Int
)

@Serializable
data class UserDetail(
    val firstName: String?,
    val lastName: String?,
    val cardFullName: String?,
    val cardNumber: String?,
    val cardExpireMonth: Int?,
    val cardExpireYear: Int?,
    val cardCVV: String?,
    val uid: Int?,
    val lastOid: Int?,
    val orderStatus: String?
)

@Serializable
data class UserUpdateParams(
    var firstName: String,
    var lastName: String,
    var cardFullName: String,
    var cardNumber: String,
    var cardExpireMonth: Int,
    var cardExpireYear: Int,
    var cardCVV: String,
    var sid: String
)

