package com.kartikasw.kelilinkseller.core.data.mapper

import com.kartikasw.kelilinkseller.core.data.source.remote.response.StoreResponse
import com.kartikasw.kelilinkseller.core.domain.model.Store

fun StoreResponse.toModel(): Store =
    Store(
        address, categories, fcm_token, id, image, lat, lon, name, operating_status, queue
    )

fun List<StoreResponse>.toListModel(): List<Store> =
    this.map { it.toModel() }