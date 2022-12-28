package com.kartikasw.kelilinkseller.core.data.mapper

import com.kartikasw.kelilinkseller.core.data.source.remote.response.SellerResponse
import com.kartikasw.kelilinkseller.core.domain.model.Seller

fun SellerResponse.toModel(): Seller =
    Seller(
        email, uid
    )