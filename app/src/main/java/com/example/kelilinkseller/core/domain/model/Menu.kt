package com.example.kelilinkseller.core.domain.model

data class Menu(
    val available: Boolean = true,
    val id: String = "",
    val image: String = "",
    val name: String,
    val price: Int,
    val store_id: String = "",
    val unit: String,
)
