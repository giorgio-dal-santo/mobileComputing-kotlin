package com.example.mangiaebasta.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseError(
    val message: String
)
