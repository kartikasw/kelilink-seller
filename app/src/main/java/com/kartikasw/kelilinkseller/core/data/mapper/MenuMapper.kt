package com.kartikasw.kelilinkseller.core.data.mapper

import com.kartikasw.kelilinkseller.core.data.source.remote.response.MenuResponse
import com.kartikasw.kelilinkseller.core.domain.model.Menu

fun MenuResponse.toModel(): Menu =
    Menu(
        available, description, id, image, name, price, store_id, unit
    )

fun List<MenuResponse>.toListModel(): List<Menu> =
    this.map { it.toModel() }