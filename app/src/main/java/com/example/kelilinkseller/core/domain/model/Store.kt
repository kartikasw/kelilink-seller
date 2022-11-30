package com.example.kelilinkseller.core.domain.model

data class Store(
    val address: String = "",
    val categories: List<String> = listOf(),
    val fcm_token: String,
    val id: String = "",
    val image: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val name: String = "",
    val operating_status: Boolean = false,
    val queue: List<String> = listOf()
)
