package com.example.smogcheck.api

import kotlinx.serialization.Serializable

@Serializable
data class Body(
    val items: List<Item>,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)