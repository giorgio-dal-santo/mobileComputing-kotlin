package com.example.mangiaebasta.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val sid: String,
    val uid: Int
)