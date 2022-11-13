package com.example.kelilinkseller.core.data.mapper

import com.example.kelilinkseller.core.data.source.remote.response.InvoiceResponse
import com.example.kelilinkseller.core.domain.model.Invoice

fun InvoiceResponse.toModel(): Invoice =
    Invoice(
        address, id,
        orders = listOf(),
        queue_order, status, store_id, store_image, store_lat, store_lon, store_name, time, total_price, user_id
    )

fun List<InvoiceResponse>.toListModel(): List<Invoice> =
    this.map { it.toModel() }