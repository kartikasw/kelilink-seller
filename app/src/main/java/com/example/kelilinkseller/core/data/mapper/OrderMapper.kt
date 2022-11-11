package com.example.kelilinkseller.core.data.mapper

import com.example.kelilinkseller.core.data.source.remote.response.OrderResponse
import com.example.kelilinkseller.core.domain.model.Order

fun OrderResponse.toModel() =
    Order(
        amount, name, note, price, total_price, unit
    )

fun List<OrderResponse>.toListModel(): List<Order> =
    this.map { it.toModel() }