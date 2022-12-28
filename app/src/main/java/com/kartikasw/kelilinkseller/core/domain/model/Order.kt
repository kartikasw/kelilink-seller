package com.kartikasw.kelilinkseller.core.domain.model

data class Order(
    val amount: Int = 0,
    val name: String = "",
    val note: String = "",
    val price: Int = 0,
    val total_price: Int = 0,
    val unit: String = ""
)