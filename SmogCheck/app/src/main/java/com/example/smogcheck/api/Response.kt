package com.example.smogcheck.api

import com.example.smogcheck.api.Body
import com.example.smogcheck.api.Header
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val body: Body,
    val header: Header
)