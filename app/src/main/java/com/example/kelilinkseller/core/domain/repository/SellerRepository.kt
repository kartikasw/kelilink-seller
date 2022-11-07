package com.example.kelilinkseller.core.domain.repository

interface SellerRepository {
    fun setFcmToken(token: String)
    fun getFcmToken(): String
}