package com.example.kelilinkseller.core.data.source.remote.response

data class StoreResponse(
    val address: String = "",
    val categories: List<String> = listOf(),
    val id: String = "",
    val image: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val name: String = "",
    val operating_status: Boolean = false,
    val queue: List<String> = listOf(),
    val total_queue: Int = 0
)