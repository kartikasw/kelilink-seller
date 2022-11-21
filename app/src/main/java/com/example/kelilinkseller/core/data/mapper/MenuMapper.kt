package com.example.kelilinkseller.core.data.mapper

import com.example.kelilinkseller.core.data.source.remote.response.MenuResponse
import com.example.kelilinkseller.core.domain.model.Menu

fun MenuResponse.toModel(): Menu =
    Menu(
        available, description, id, image, name, price, store_id, unit
    )

fun List<MenuResponse>.toListModel(): List<Menu> =
    this.map { it.toModel() }