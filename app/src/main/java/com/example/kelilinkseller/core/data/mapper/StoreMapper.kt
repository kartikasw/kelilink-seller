package com.example.kelilinkseller.core.data.mapper

import com.example.kelilinkseller.core.data.source.remote.response.StoreResponse
import com.example.kelilinkseller.core.domain.model.Store

fun StoreResponse.toModel(): Store =
    Store(
        address, categories, fcm_token, id, image, lat, lon, name, operating_status, queue, total_queue
    )

fun List<StoreResponse>.toListModel(): List<Store> =
    this.map { it.toModel() }