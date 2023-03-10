package com.kartikasw.kelilinkseller.core.domain.model

data class Menu(
    val available: Boolean = true,
    val description: String = "",
    val id: String = "",
    val image: String = "",
    val name: String,
    val price: Int,
    val store_id: String = "",
    val unit: String,
)
