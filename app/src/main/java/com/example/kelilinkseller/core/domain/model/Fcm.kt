package com.example.kelilinkseller.core.domain.model


data class Fcm(
    val to: String = "",
    val data: FcmData
)

data class FcmData(
    val invoice_id: String = "",
    val store_id: String = "",
    val store_name: String = ""
)