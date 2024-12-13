package com.example.mangiaebasta.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseError(
    val message: String
)
