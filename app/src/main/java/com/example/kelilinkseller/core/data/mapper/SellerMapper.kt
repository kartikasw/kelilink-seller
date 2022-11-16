package com.example.kelilinkseller.core.data.mapper

import com.example.kelilinkseller.core.data.source.remote.response.SellerResponse
import com.example.kelilinkseller.core.domain.model.Seller

fun SellerResponse.toModel(): Seller =
    Seller(
        email, uid
    )