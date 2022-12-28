package com.kartikasw.kelilinkseller.core.data.mapper

import com.kartikasw.kelilinkseller.core.data.source.remote.response.InvoiceResponse
import com.kartikasw.kelilinkseller.core.domain.model.Invoice

fun InvoiceResponse?.toModel(): Invoice =
    Invoice(
        this!!.address, id,
        orders = listOf(),
        queue_order, status, store_id, store_image, store_lat, store_lon, store_name, time, time_expire, total_price, user_fcm_token, user_id, user_phone_number
    )

fun List<InvoiceResponse?>.toListModel(): List<Invoice> =
    this.map { it.toModel() }