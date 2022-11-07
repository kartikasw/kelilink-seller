package com.example.kelilinkseller.core.domain.use_case.seller

interface SellerUseCase {
    fun setFcmToken(token: String)
    fun getFcmToken(): String
}