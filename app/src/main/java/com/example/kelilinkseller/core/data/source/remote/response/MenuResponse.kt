package com.example.kelilinkseller.core.data.source.remote.response

data class MenuResponse(
    val available: Boolean = false,
    val id: String = "",
    val image: String = "",
    val name: String = "",
    val price: Int = 0,
    val store_id: String = "",
    val unit: String = ""
)