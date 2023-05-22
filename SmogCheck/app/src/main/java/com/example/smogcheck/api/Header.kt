package com.example.smogcheck.api

import kotlinx.serialization.Serializable

@Serializable
data class Header(
    val resultCode: String,
    val resultMsg: String
)