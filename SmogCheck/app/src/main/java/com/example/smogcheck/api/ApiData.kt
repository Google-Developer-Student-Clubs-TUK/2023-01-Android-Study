package com.example.smogcheck.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiData(
    val response: Response
)