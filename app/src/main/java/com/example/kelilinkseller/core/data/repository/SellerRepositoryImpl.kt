package com.example.kelilinkseller.core.data.repository

import com.example.kelilinkseller.core.data.source.local.LocalDataSource
import com.example.kelilinkseller.core.domain.repository.SellerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SellerRepositoryImpl @Inject constructor(
    private val local: LocalDataSource
): SellerRepository {
    override fun setFcmToken(token: String) {
        local.setFcmToken(token)
    }

    override fun getFcmToken(): String =
        local.getFcmToken()!!
}