package com.example.kelilinkseller.core.domain.use_case.seller

import com.example.kelilinkseller.core.domain.repository.SellerRepository
import javax.inject.Inject

class SellerInteractor @Inject constructor(
    private val sellerRepository: SellerRepository
) : SellerUseCase {
    override fun setFcmToken(token: String) {
        sellerRepository.setFcmToken(token)
    }

    override fun getFcmToken(): String =
        sellerRepository.getFcmToken()
}